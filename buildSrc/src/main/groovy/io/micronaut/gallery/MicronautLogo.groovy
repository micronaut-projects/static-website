package io.micronaut.gallery

import groovy.transform.CompileStatic
import io.micronaut.core.naming.NameUtils

@CompileStatic
class MicronautLogo implements MicronautLogoHtml {
    static final String IMAGE_PATH = '/images/micronaut-logos/'

    String asset
    String label
    String background

    String getSrc() {
        "${IMAGE_PATH}${asset}"
    }

    String getDownload() {
        String fileName = NameUtils.filename(asset)
        src.replace(fileName, "${fileName}-full")
    }

    String getSvg() {
        String extension = NameUtils.filename(asset)
        src.replace(".${extension}", ".svg")
    }
}
