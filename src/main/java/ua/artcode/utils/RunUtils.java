package ua.artcode.utils;

import ua.artcode.exceptions.LessonNotFoundException;
import ua.artcode.model.Course;
import ua.artcode.model.Lesson;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by v21k on 16.04.17.
 */
public class RunUtils {

    private static final JavaCompiler COMPILER = getSystemJavaCompiler();

    // todo use logger
    private static JavaCompiler getSystemJavaCompiler() {
        return ToolProvider.getSystemJavaCompiler();
    }

    public static String compile(String[] classPaths) throws IOException {
        try (ByteArrayOutputStream baosErr = new ByteArrayOutputStream()) {
            COMPILER.run(null, null, baosErr, classPaths);
            return baosErr.toString();
        }
    }

    public static Class<?> getClass(String className, String classRoot) throws MalformedURLException,
            ClassNotFoundException {
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File(classRoot).toURI().toURL()});
        return Class.forName(className, true, classLoader);
    }

    // todo extract to correspond place
    /**
     * Lesson packages starts with "_number_"
     * For numbers < 10 it looks like "_02_lesson" etc, so if lesson number is < 10,
     * we need to add "0" to it's String.valueOf()
     * Otherwise - do not add anything.
     * */
    public static Lesson getLesson(int lessonNumber, Course course) throws LessonNotFoundException {
        return course.getLessons()
                .stream()
                .filter(lsn -> lsn.getName().contains(lessonNumber < 10 ? "0" + lessonNumber : String.valueOf(lessonNumber)))
                .findFirst()
                .orElseThrow(() -> new LessonNotFoundException("No lesson found with number :" + lessonNumber));
    }
}
