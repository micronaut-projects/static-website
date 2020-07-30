package io.micronaut.gallery

import io.micronaut.core.naming.NameUtils

class MicronautLogo implements MicronautLogoHtml {
    static final String IMAGE_PATH = '/images/micronaut-logos/'

    String asset
    String label
    String background

    String getSrc() {
        return IMAGE_PATH + asset
    }

    String getDownload() {
        def fileName = NameUtils.filename(asset)
        return src.replace(fileName, fileName+"-full")
    }

    String getSvg() {
        def extension = NameUtils.filename(asset)
        return src.replace("." + extension, ".svg")
    }
}
