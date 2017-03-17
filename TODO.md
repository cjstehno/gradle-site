
```groovy
projectDocs {
    owner {
        name 'Christopher J. Stehno'
        email 'chris@stehno.com'
    }
    
    features {
        licensing true
        preview true
        coverage true
        quality true
        docs true
        guide true
    }
}
```

The `features` closure provides a DSL extension to allow toggling of features so that configuration may be defined in the project itself.

    
## `jacoco` plugin (also `coveralls`)

When the `jacoco` plugin is present (with optional `coveralls` existence):

* the reports will be included in the site build
* apply the config

    jacocoTestReport {
        reports {
            xml.enabled = true // if coveralls is also present
            html.enabled = true
        }
    }

## `codenarc` plugin

If the `codenarc` plugin is present:

* nag logging if the codenarc config files are not setup
* Add `installCodeNarcConfig` to install config files
* reports are included in site
* apply the codenarc config (see projects)

## `groovydoc` plugin

If the groovydoc plugin is configured:

* apply the config
* reports are included in site

## `javadoc` plugin

* apply the config (including optional asciidoc)
* reports are included in site

## `asciidoctor` plugin

* content is included in site

----