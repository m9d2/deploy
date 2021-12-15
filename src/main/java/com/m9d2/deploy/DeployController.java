package com.m9d2.deploy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Semaphore;
import java.util.stream.Stream;

@Slf4j
@RestController()
public class DeployController {

    private final Semaphore semaphore = new Semaphore(1);

    @GetMapping(value = {"deploy"}, produces = {"text/event-stream"})
    public Flux<String> deploy(@RequestParam String path) {
        log.info("exec script [{}]", path);
        File file = new File(path);
        if (!file.exists()) {
            return Flux.fromStream(Stream.of(this.println("执行的脚本不存在"), this.println("test")));
        } else if (this.semaphore.tryAcquire()) {
            Process process = exec(path);
            if (process == null) {
                return Flux.fromStream(Stream.of(this.println("部署失败")));
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return Flux.fromStream(br.lines()).map(this::println).doFinally((type) -> {
                this.semaphore.release();
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            return Flux.fromStream(Stream.of(this.println("另一个用户正在部署，请稍后重试")));
        }
    }

    private Process exec(String sh) {
        try {
            Runtime.getRuntime().exec("chmod a+x " + sh);
            return Runtime.getRuntime().exec(sh);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String println(String line) {
        return line;
    }
}
