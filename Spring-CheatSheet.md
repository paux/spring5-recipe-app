# Spring Framework Cheat-Sheet

by Stephan Paukner &lt;paux+dev@paukner.cc&gt;, Dec 2020. Work in progress.

Disclaimer: Expect errors

## Inversion of Control (IoC)

When you write classes for components of your web app,
the framework instantiates these classes, you don't instantiate them yourself.
What is started up is the _framework,_ and not your classes.
It's not you in charge of the framework or libraries, it's the other way round.

## Dependency Injection (DI)

When one of your components needs another, you request an instance of it from the framework.
Again, you don't create an instance yourself.

There are three possibilities to have a dependency injected:

1. By constructor, that is, by constructor argument; this is the recommended approach.
   You have to store that instance away, e.g. in a private property.
   Since only the framework ever uses the constructor, it has to figure out how to provide the argument.
2. By public property. You need the `@Autowired` annotation for this.
3. By private property. Do not do this.

Since test classes are no Spring components (they're only relevant for Continuous Integration),
typically #2 is used there. For the runtime context, use #1.

### Injection by constructor

#### Java example

```java
import org.springframework.stereotype.Component;

@Component
public class SomeComponent {
}

@Component
public class OtherComponent {
    
    private SomeComponent someComponent;
    
    OtherComponent(SomeComponent someComponent) {
        this.someComponent = someComponent;
    }
}
```

#### Supplementary Angular example

```typescript
@Service
class SomeService {
}

@Component
class SomeComponent {

   constructor(private someService: SomeService) {
   }
}
```

### Injection by property

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SomeComponent {
}

public class SomeTest {

   @Autowired
   SomeComponent someComponent;
}
```

## Convention over Configuration (CoC)

All external dependencies are readily available with their own and _curated_ dependencies,
and they're ready to use with a default and _curated_ configuration.
You can override these defaults, but you have to do so explicitly.

## Annotations

While there are Java-internal annotations (like `@Override` to implement a method of an interface), annotations
are typically used to outsource non-business logic, boilerplate code, to other libraries. These annotations get translated
into extended Java code at compile time, so that the specified functionality is implemented and magically available.

E.g., `@Slf4j` on a class creates a `log` property in it with an instance of a full-fledged logger (see CoC).

`@Getters` and `@Setters` (from Project Lombok) on a class create getter and setter methods (e.g. `void setSomething(Something something)`
and `Something getSomething()`) for every property (e.g. `Something something`) in that class.

The advantage is that there is less code to review, especially code that just creates additional helpers that do not
cover business logic.

## Spring Components

To declare a class as a Spring component, use the `@Component` annotation:

```java
import org.springframework.stereotype.Component;

@Component
public class MyUnspecificComponent {
}
```
If you omit this (or one of the other annotations below), Spring doesn't know about that class.

### Controllers

The `@Controller` annotation works like `@Component`, but it makes the intent clearer
that this is a web controller.
Typically, expect a `@RequestMapping` on the class (to declare a common sub-path)
or on its methods (to declare a specific HTTP endpoint).

```java
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

   @RequestMapping({"", "/", "/index"})
   public Set<Recipe> listAllRecipes() {
      // ...
   }
}
```

Web controllers should do just that: handle web requests. They should _not_ contain business logic.

`@PathVariable` annotates the controller method argument that matches the pattern given in
`@RequestMapping`:

```java
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SomeController {
    
    @RequestMapping("/{id}/details")
    public String showDetails(@PathVariable String id, Model model) {
        // ...
    }
}
```

`@ModelAttribute` annotates the DTO as a controller method argument, what tells Spring to
bind the form POST parameters to this DTO; therefore, a `@PostMapping` is also involved:

```java
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SomeController {

   @PostMapping
   @RequestMapping("/save")
   public String saveSomething(@ModelAttribute SomeDto someDto) {
      // ...
   }
}
```

## Services

Services contain business logic. Usually, a service implements an interface, because then the components that use that service
only have to refer to that interface. By this, both the requesting component and the service itself refer to a common dependency
(that interface), instead of the component depending on a specific implementation of the service.

By this, Spring can decide _which implementation to provide to the requesting component._

By convention, interfaces in Java have a meaningful name like `SomeService`, in contrast to the C# folks that prefer `ISomeService`.
The (default) implementation is then named `SomeServiceImpl` or `DefaultSomeService`, in contrast to the C# folks that
give it the original meaningful name `SomeService`.

### Bad design

```java
@Service
public class SomeServiceImpl {
}

@Component
public class BadComponent {
    
    private SomeServiceImpl someServiceImpl;
}

```

component → implementation

### Good design

```java
public interface SomeService {

   void anyMethod();
}

@Service
public class SomeServiceImpl implements SomeService {

   @Override
   void anyMethod() {
      // ...
   }
}

@Component
public class GoodComponent {

   private SomeService someService;
}
```

component → interface ← implementation

## Repositories

## Profiles

## Tests

### Unit Tests

### Integration Tests
