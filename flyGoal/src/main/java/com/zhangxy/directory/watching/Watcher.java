package com.zhangxy.directory.watching;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Watcher {
    //文件名称以xls结尾
    final Predicate<Path> is_XLS = (n) -> n.getFileName().toString().endsWith("xls");
    //文件名称以xlsx结尾
    final Predicate<Path> is_XLSX = (n) -> n.getFileName().toString().endsWith("xlsx");

    public void test() throws IOException, InterruptedException {

        Watcher.log.info("监控开始");
        final WatchService watcher = FileSystems.getDefault().newWatchService();
        Paths.get("C:\\baseDataExcelCopy").register(watcher,
                StandardWatchEventKinds.ENTRY_CREATE);
        Paths.get("C:\\var").register(watcher,
                StandardWatchEventKinds.ENTRY_CREATE);
        while (true) {
            final WatchKey key = watcher.take();
            key.pollEvents().stream().forEach(event -> {
                final Path patch = (Path) event.context();
                System.out.println(patch.toFile().getName() + " comes to " + event.kind());
                try {
                    this.action();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            });
            final boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }

    }

    public void action() throws IOException {
        final Path filePath = Paths.get("C:\\baseDataExcelCopy");
        final Map<String, List<Path>> paths = Files.walk(filePath, 1)//遍历文件目录
                .filter(path -> !path.equals(filePath))//过滤当前文件目录，获取目录下的所有文件
                .filter(this.is_XLS.or(this.is_XLSX))//过滤目录是xls或xlsx的文档
                .collect(Collectors //根据文件名称进行分组
                        .groupingBy(p -> p.getFileName().toString().substring(0, this.getIndex(p.toFile()))));
        //遍历批次分组
        paths.forEach((batchNo, patchList) -> {
            if ((patchList.size() == 2)) {//一个批次同时存在俩个文件才会解析
                Watcher.log.info(batchNo+"文件数量："+patchList.size());
            } else {
                Watcher.log.warn(batchNo + "文件数量异常，文件数量：" + patchList.size());
            }
        });
        System.out.println(paths.size());
    }

    private int getIndex(final File file) {
        int index = file.getName().indexOf("(");
        if (index < 0) {
            index = file.getName().indexOf("（");
        }
        return index;
    }
}
