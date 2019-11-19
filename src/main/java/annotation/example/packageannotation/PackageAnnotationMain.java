package annotation.example.packageannotation;

import annotation.example.packageannotation.api.annotation.PackageAnnotation;

public class PackageAnnotationMain {

    public static void main(String[] args) {
        PackageAnnotation annotation = PackageAnnotationMain.class.getPackage().getAnnotation(PackageAnnotation.class);
        if(annotation != null) {
            System.out.println(annotation.value());
        } else {
            System.out.println("No package annotation!");
        }
    }

}
