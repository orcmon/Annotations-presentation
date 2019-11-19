package annotation.example.basic;

import annotation.example.basic.api.annotation.InheritedAnnotation;
import annotation.example.basic.model.InheritedClass;

public class BasicMain {

    public static void main(String[] args) {
        InheritedAnnotation inheritedAnnotation = InheritedClass.class.getAnnotation(InheritedAnnotation.class);

        if(inheritedAnnotation != null) {
            System.out.println("inherited class has inherited annotation");
        } else {
            throw new RuntimeException("No inherited annotation");
        }

    }

}
