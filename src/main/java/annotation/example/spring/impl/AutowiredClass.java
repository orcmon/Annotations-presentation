package annotation.example.spring.impl;

import annotation.example.spring.api.annotation.Component;
import annotation.example.basic.api.annotation.CustomAnnotation;

@CustomAnnotation
@Component
class AutowiredClass {
    public AutowiredClass() {}
}
