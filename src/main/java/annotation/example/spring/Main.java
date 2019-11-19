package annotation.example.spring;

import annotation.example.spring.api.annotation.Autowired;
import annotation.example.spring.config.ClassScanner;
import annotation.example.spring.impl.ImplClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static Map<Class<?>, Object> managedClassesInstances = new HashMap<>();
    private static Map<Class<?>, List<Field>> dependenciesMap = new HashMap<>();

    public static void main(String[] args) {
        List<Class<?>> projectClasses = ClassScanner.getProjectClasses();

        fillDependenciesMap(projectClasses);

        dependenciesMap.entrySet()
                .stream()
                .peek(entry -> entry.getValue().stream().map(Field::getType).forEach(Main::initiateClass))
                .forEach(entry -> {
                    initiateClass(entry.getKey());
                    Object o = managedClassesInstances.get(entry.getKey());
                    entry.getValue().forEach(field -> {
                        field.setAccessible(true);
                        try {
                            field.set(o, managedClassesInstances.get(field.getType()));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    });
                });

        Object implClassInstance = managedClassesInstances.get(ImplClass.class);
        if (implClassInstance != null) {
            if(((ImplClass) implClassInstance).getAutowiredClass() != null) {
                System.out.println(((ImplClass) implClassInstance).getAutowiredClass() + " is the instance!");
                System.exit(0);
            }
        }
        throw new RuntimeException("Failure!");

    }

    /**
     * Write code to fill {@link #dependenciesMap}.
     * for each class iterate through it's declared fields and add types of fields annotated with {@link Autowired} .
     */
    private static void fillDependenciesMap(List<Class<?>> classes) {
        for (Class<?> manageClass : classes) {
            List<Field> autowiredFields = new ArrayList<>();

            Field[] declaredFields = manageClass.getDeclaredFields();
            for (Field field : declaredFields) {
//          filter fields with autowired annotation.
                if (false) {
                    autowiredFields.add(field);
                    dependenciesMap.put(manageClass, autowiredFields);
                }
            }
        }
    }

    private static void initiateClass(Class<?> clazz) {
        if (managedClassesInstances.containsKey(clazz)) return;
        try {
            Constructor<?> constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            managedClassesInstances.put(clazz, constructor.newInstance());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }



}
