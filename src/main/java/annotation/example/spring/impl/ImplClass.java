package annotation.example.spring.impl;

import annotation.example.spring.api.annotation.Autowired;
import annotation.example.spring.api.annotation.Component;

@Component
public class ImplClass {

    @Autowired
    private AutowiredClass autowiredClass;

    public AutowiredClass getAutowiredClass() {
        return autowiredClass;
    }
}
