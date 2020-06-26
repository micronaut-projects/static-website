title: Micronaut now supports Microsoft Azure Functions
date: May 15, 2020
description: The Micronaut team at Object Computing is pleased to announce that Micronaut 2.0.0.M3 now features support for developing serverless applications with Azure Functions.
author: Sergio del Amo (Object Computing Grails & Micronaut Team)
image: 2020-05-15.png
CSS: https://micronaut.io/stylesheets/prismjs.css
JAVASCRIPT: https://micronaut.io/javascripts/prismjs.js
---

# [%title]

[%author]

[%date]

#azure #serverless 

The [Micronaut team](https://objectcomputing.com/products/2gm-team) at Object Computing is pleased to announce that [Micronaut 2.0.0.M3](https://objectcomputing.com/news/2020/04/30/micronaut-20-m3-big-boost-serverless-and-micronaut-launch) now features support for developing serverless applications with [Azure Functions](https://azure.microsoft.com/en-us/services/functions/). If you want to develop [Azure Functions with Java](https://docs.microsoft.com/en-us/azure/azure-functions/functions-reference-java), you'll find that you can dramatically improve your productivity and build incredibly fast and lightweight applications with the Micronaut framework.  

Micronaut's Azure-friendly features include support for using [dependency injection](https://docs.micronaut.io/latest/guide/index.html#ioc), validation, and [AOP annotations](https://docs.micronaut.io/latest/guide/index.html#aop) within your serverless code. 

## Two Micronaut Azure Modules

Micronaut offers two modules to help you with the development of Azure Functions:

- **Simple Azure Functions**.[io.micronaut.azure:micronaut-azure-function](https://micronaut-projects.github.io/micronaut-azure/snapshot/guide/index.html#simpleAzureFunctions)
- **Azure HTTP Functions**. [io.micronaut.azure:micronaut-azure-function-http](https://micronaut-projects.github.io/micronaut-azure/snapshot/guide/index.html#azureHttpFunctions)

### Micronaut Simple Azure Functions

[Azure Functions support many triggers](https://docs.microsoft.com/en-us/azure/azure-functions/functions-triggers-bindings) beyond HTTP, including Queue, Timer, and Blob storage. Simple Azure Functions are for these non-HTTP triggers.

Moreover, if your function only needs to bind to a single HTTP endpoint, you may choose Simple Azure Functions as well.

You can use dependency injection and validation within your functions as illustrated below:

```java
public interface NameTransformer {
   @NonNull
   String transform(@NonNull @NotBlank String name);
}
 
@Singleton
public CapitalizeNameTransformer implements NameTransformer {
  @Override
  @NonNull
  public String transform(@NonNull @NotBlank String name) {
      return StringUtils.capitalize(name);
  } 
}
 
public class NameTransformFunction extends AzureFunction {
    @Inject // 1️⃣
    NameTransformer nameTransformer
 
    public String echo(
          @HttpTrigger(name = "req", 
                       methods = HttpMethod.GET, 
                       authLevel = AuthorizationLevel.ANONYMOUS) 
           HttpRequestMessage<Optional<String>> request, 
           ExecutionContext context) {
        try {
            String name = request.getQueryParameters().get("name");
            return nameTransformer.transform(name);
        } catch(ConstraintViolationException e) { // 2️⃣
            return "The supplied name must be not blank"; 
        }
    }
}
```

1️⃣ User can dependency inject fields with `@Inject`.  
2️⃣ `Name::transformer` parameter constraints are enforced.

### Micronaut Azure Http Functions

If your function needs to respond to multiple HTTP endpoints, or if you want to write regular Micronaut controllers and have them executed using an Azure Function, you can choose Azure HTTP Functions.

With the Micronaut Azure HTTP Functions module, for the previous example, instead of NameTransformFunction, you will have a controller:

```java
@Controller("/")
public NameController {
 
    private final NameTransformer transformer;
 
    public NameController(NameTransformer transformer) { // 1️⃣
        this.transformer = transformer;
    }
 
    @Produces(MediaType.TEXT_PLAIN)
    @Get
    public String index(@Nullable String name) {
        try {
            return transformer.transform(name);
        } catch(ConstraintViolationException e) {
            return "The supplied name must be not blank"; 
        }
    }
}
```

1️⃣ Micronaut supports constructor based injection as well. 

In addition, testing Micronaut Azure Functions is easy. The next test shows how to test the previous controller:


```java
public class NameControllerTest {
 
    @Test
    public void testNameSupplied() throws Exception {
        try (Function function = new Function()) {
            HttpRequestMessageBuilder.AzureHttpResponseMessage response =
                function.request(HttpMethod.GET, "/")
                        .parameter("name", "sergio")
                        .invoke();
            assertEquals(HttpStatus.OK, response.getStatus());
            assertEquals("Sergio", response.getBodyAsString());
        }
    }
}
```

## Next Steps

- Read [Micronaut Azure Module](https://micronaut-projects.github.io/micronaut-azure/snapshot/guide/index.html) documentation.
- Try it out at [Micronaut Launch](https://micronaut.io/launch) by choosing the azure-function feature!
- Check [Azure Functions Library](https://github.com/Azure/azure-functions-java-library) documentation. This library is included when you select azure-function while generating a Micronaut application with [Micronaut Launch](https://micronaut.io/launch)
- Micronaut is build-tool agnostic. You can write apps with Maven or Gradle. If you write Micronaut Azure Functions with Gradle, you will need to learn how to use the [Azure Gradle Plugin](https://plugins.gradle.org/plugin/com.microsoft.azure.azurefunctions).

As you'll quickly see, by combining Micronaut and Azure, you don't have to give up the software paradigms that make you productive and keep your code robust when coding Java serverless functions.
