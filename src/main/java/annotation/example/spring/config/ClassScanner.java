package annotation.example.spring.config;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassScanner {

    public static List<Class<?>> getProjectClasses() {
        URL directory = getProjectUrl();

        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{directory}, ClassScanner.class.getClassLoader());

        List<Class<?>> classes = null;
        try {
            classes = Files.walk(Paths.get(directory.toURI()))
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .map(string -> string.replace(directory.getFile().replaceFirst("/", "").replaceAll("/", "\\\\"), ""))
                    .map(string -> string.replace(".class", ""))
                    .map(string -> string.replaceAll("\\\\", "."))
                    .filter(string -> !string.contains("kotlin"))
                    .map(string -> {
                        try {
                            return urlClassLoader.loadClass(string);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return classes;
    }

    private static URL getProjectUrl() {
        return Stream.of(((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs()).filter(url -> {
            try {
                return Files.isDirectory(Paths.get(url.toURI()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return false;
        }).findAny().orElseThrow(RuntimeException::new);
    }

}
