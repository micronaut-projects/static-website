package io.micronaut.main.pages

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.MarkupBuilder
import io.micronaut.Navigation
import io.micronaut.MenuItem
import io.micronaut.pages.Page

@CompileStatic
class HomePage extends PageWithSyntaxHighlight {
    String slug = 'index.html'
    String bodyClass = 'home'
    String title = null

    @Override
    MenuItem menuItem() {
        Navigation.mobileHomeMenuItem(micronautUrl())
    }


    @Override
    @CompileDynamic
    String mainContent() {
        StringWriter writer = new StringWriter()
        MarkupBuilder html = new MarkupBuilder(writer)
        html.div(class: 'container') {
            article {
                div(id: 'logo') {
                    a(href: micronautUrl()) {
                        img src: "${getImageAssetPreffix()}micronaut.svg", alt: 'micronaut framework'
                    }
                }
            }
            div(id: "calltoaction") {
                h2 'A modern, JVM-based, full-stack framework for building modular, easily testable microservice and serverless applications.'
            }
        }
        html.mkp.yieldUnescaped('<div class="starsbgtobrownelipse brownbg whitecircleborderseparator"></div>')

        html.div(class: 'brown') {
            html.div(class: 'twocolumns container', id: 'rocketlanguages') {

                div(class: 'column') {

                    html.article(class: 'feature') {
                        header {
                            h2 {
                                mkp.yieldUnescaped 'Polyglot Framework'
                            }
                        }
                        p(class: 'align-center language') {
                            img src: "${imageAssetPreffix}circle_java.svg", alt: "Java Logo"
                            span 'Java'
                        }
                        p(class: 'align-center language') {
                            img src: "${imageAssetPreffix}circle_groovy.svg", alt: "Groovy Logo"
                            span 'Groovy'
                        }
                        p(class: 'align-center language') {
                            img src: "${imageAssetPreffix}circle_kotlin.svg", alt: "Kotlin Logo"
                            span 'Kotlin'
                        }
                    }
                }
                div(class: 'column') {
                    html.article(class: 'feature') {
                        p {
                            img style: 'width: 100%;', src: "${imageAssetPreffix}rocket.svg", alt: "Micronaut - Minimal memory footprint"
                        }
                    }
                }
            }
        }

        html.mkp.yieldUnescaped('<div class="brownbgwhiteoval whitecircleborderseparator"></div>')

        html.div(class: 'white') {
            html.article(id: "startup", class: 'feature container') {
                header {
                    h2 {
                        mkp.yieldUnescaped 'Fast Startup Time &nbsp;&nbsp;&mdash;&nbsp;&nbsp; Low Memory Consumption'
                    }
                }
                section {
                    p(id: 'rocketbg') {
                        mkp.yield('Reflection-based IoC frameworks load and cache reflection data for every single field, method, and constructor in your code, whereas with Micronaut, your application startup time and memory consumption are not bound to the size of your codebase.')
                    }
                    p(id: 'rocketimage') {
                        img src: "${imageAssetPreffix}rocketspaths.svg", alt: 'Micronaut startup time'
                    }
                }
            }
            html.article(class: 'feature container') {
                html.div(class: 'twocolumns container') {
                    div(class: 'column') {
                        html.article(class: 'feature') {
                            header {
                                h2 'Micronaut for GraalVM'
                            }
                            section {
                                p {
                                    b ' Micronaut apps startup in tens of milliseconds with GraalVM!'
                                }
                                p {
                                    mkp.yield 'Micronaut features a Dependency Injection and Aspect-Oriented Programming runtime that uses no reflection. This makes it easier for '
                                    a href: 'https://docs.micronaut.io/latest/guide/index.html#graal', 'Micronaut applications to run on GraalVM.'

                                }
                            }
                        }
                    }
                    div(class: 'column') {
                        html.article(class: 'feature') {
                            section {
                                p {
                                    img src: "${imageAssetPreffix}graalvm.svg", alt: 'GraalVM', style: 'width: 300px !important;'
                                }
                                p {
                                    a href: 'http://www.graalvm.org', 'GraalVM'
                                    mkp.yield ' is a new universal virtual machine from Oracle that supports a polyglot runtime environment and the ability to compile Java applications down to native machine code.'
                                }
                            }
                        }
                    }
                }
            }
        }


        html.mkp.yieldUnescaped('<div class="brownbg whitecircleborderseparator"></div>')

        html.div(class: 'brown') {

            html.div(class: 'twocolumns container') {
                div(class: 'column') {
                    article(class: 'feature') {
                        header {
                            h2 {
                                mkp.yieldUnescaped 'Non-blocking HTTP server built on Netty'
                            }
                        }
                        section {
                            p {
                                mkp.yieldUnescaped 'With a smooth learning curve, Micronaut\'s HTTP server makes it as easy as possible to expose APIs that can be consumed by HTTP clients.'
                            }
                            pre {
                                code(class: "language-java") {
                                    mkp.yieldUnescaped '''
import io.micronaut.http.annotation.*;

@Controller("/hello") 
public class HelloController {

    @Get("/") 
    public String index() {
        return "Hello World"; 
    }
}
'''
                                }
                            }
                        }
                    }
                }
                div(class: 'column') {
                    article(class: 'feature') {
                        header {
                            h2 {
                                mkp.yieldUnescaped 'Declarative, Reactive, Compile-Time HTTP Client'
                            }
                        }
                        section {
                            p {
                                mkp.yieldUnescaped 'Declaratively build reactive HTTP clients, which are implemented at compile-time, reducing memory consumption.'
                            }
                            pre {
                                code(class: "language-java") {
                                    mkp.yieldUnescaped '''
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.Client;
import io.reactivex.Single;

@Client("/hello")
public interface HelloClient {

    @Get("/")
    Single<String> hello();
}
'''
                                }
                            }
                        }
                    }
                }
            }
        }

        html.mkp.yieldUnescaped('<div class="brownbgwhiteoval whitecircleborderseparator"></div>')

        html.div(class: 'white') {
            html.article(class: 'feature container') {
                header {
                    h2 {
                        mkp.yieldUnescaped 'Fast and Easy Testing'
                    }
                }
                section {
                    p {
                        mkp.yieldUnescaped 'Easily spin up servers and clients in your unit tests, and run them instantaneously.'
                    }
                    pre {
                        code(class: "language-java") {
                            mkp.yieldUnescaped '''
import io.micronaut.test.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class HelloClientSpec extends Specification {

    @Inject
    HelloClient client

    void "test hello world response"() {
        expect:
        client.hello().blockingGet() == "Hello World"
    }
}
'''
                        }
                    }
                }
            }
        }

        html.mkp.yieldUnescaped('<div class="brownbg whitecircleborderseparator"></div>')

        html.div(class: 'brown') {

            html.div(class: 'twocolumns container') {
                div(class: 'column') {
                    html.article(class: 'feature') {
                        header {
                            h2 {
                                mkp.yieldUnescaped 'Build Fully Reactive and Non-Blocking Apps'
                            }
                        }
                        section {
                            p(style: 'min-height: 110px;') {
                                mkp.yieldUnescaped "Micronaut supports any framework that implements Reactive Streams, including RxJava, and Reactor."
                            }
                            pre {
                                code(class: "language-java") {
                                    mkp.yieldUnescaped """
@Client( id = "person-service" )
public interface PersonClient {
    public Single&lt;Person&gt;
        save(@Body Single&lt;Person&gt;person)
}"""
                                }
                            }
                        }
                    }
                }
                div(class: 'column') {
                    html.article(class: 'feature') {
                        header {
                            h2 {
                                mkp.yieldUnescaped 'Efficient Compile-Time Dependency Injection and AOP'
                            }
                        }
                        section {
                            p(style: 'min-height: 110px;') {
                                mkp.yieldUnescaped 'Micronaut provides a simple compile-time aspect-oriented programming API that does not use reflection.'
                            }
                            pre {
                                code(class: "language-java") {
                                    mkp.yieldUnescaped """
@Scheduled(fixedRate = "5m")
@Retry(attempts='5')
void everyFiveMinutes() {
    messageService.sendMessage("Hello World");
}"""
                                }
                            }
                        }
                    }
                }
            }
        }

        html.mkp.yieldUnescaped('<div class="brownbgwhiteoval whitecircleborderseparator"></div>')

        html.div(class: 'white') {
            html.article(class: 'feature container') {
                header {
                    h2 {
                        mkp.yieldUnescaped 'Natively Cloud Native'
                    }
                }
                section {
                    p {
                        mkp.yieldUnescaped micronaut()
                        mkp.yieldUnescaped '\'s Cloud support is built right in, including support for common discovery services, distributed tracing tools, and cloud runtimes.'
                    }
                    p {
                        img src: "${imageAssetPreffix}cloudservices.svg", alt: "Micronaut Cloud Services Integration"
                    }
                }
            }
        }

        html.mkp.yieldUnescaped('<div class="brownbg whitecircleborderseparator"></div>')

        html.div(class: 'brown') {

            html.div(class: 'twocolumns container') {
                div(class: 'column') {
                    html.article(class: 'feature') {
                        header {
                            h2 {
                                mkp.yieldUnescaped 'Designed for Building Resilient Microservices'
                            }
                        }
                        section {
                            p {
                                mkp.yieldUnescaped 'Distributed environments require planning for failure. Micronaut\'s built-in support for retry, circuit breaker, and fallbacks help you plan.'
                            }
                            pre {
                                code(class: "language-java") {
                                    mkp.yieldUnescaped '''
import io.micronaut.retry.annotation.*

@CircuitBreaker(reset = "30s")
public List<Book> findBooks() {
    ...
    ..
}
'''
                                }
                            }
                        }
                    }
                }
                div(class: 'column') {
                    html.article(class: 'feature') {
                        header {
                            h2 {
                                mkp.yieldUnescaped 'Ready to develop serverless applications'
                            }
                        }
                        section {
                            p {
                                mkp.yieldUnescaped 'Micronaut\'s low overhead compile-time DI and AOP make it perfect for writing functions for serverless environments like '
                                a(href: 'https://aws.amazon.com/lambda/', 'AWS Lambda')
                                mkp.yieldUnescaped '.'
                            }
                            pre {
                                code(class: "language-java") {
                                    mkp.yieldUnescaped '''
@Field
@Inject
HelloService helloService

Message hello(Person person) {
    helloService.hello(person)
}
'''
                                }
                            }
                        }
                    }
                }
            }

        }

        html.mkp.yieldUnescaped('<div class="brownbgwhiteoval whitecircleborderseparator"></div>')

        html.div(class: 'white') {
            html.article(class: 'feature container') {
                header {
                    h2 {
                        mkp.yieldUnescaped 'Fast Data-Access Configuration'
                    }
                }
                section {
                    p {
                        mkp.yieldUnescaped micronaut()
                        mkp.yieldUnescaped ' provides sensible defaults that automatically configure your favourite data access toolkit and APIs to make it easy to write your own integrations.'
                    }
                    p {
                        img src: "${imageAssetPreffix}fastconfiguration.svg", alt: "Micronaut Fast Data Access Configuration"
                    }
                }
            }
        }

        writer.toString()
    }
}
