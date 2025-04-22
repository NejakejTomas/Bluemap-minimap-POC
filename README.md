Needed to add:

- Jitpack to repositories
- ```
  configurations.all {
    attributes {
        attribute(Attribute.of("ui", String::class.java), "awt")
    }
   }
  ``` 
  to build.gradle
- Create custom main dispatcher (`MainDispatcher`, `CustomMainDispatcherFactory`)