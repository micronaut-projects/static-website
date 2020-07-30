package io.micronaut

import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.core.naming.NameUtils
import org.yaml.snakeyaml.Yaml

class YamlConfToJson {
    File file;

    YamlConfToJson(File file) {
        this.file = file
    }

    String toJson() {
        Yaml yaml = new Yaml()
        Map model = yaml.load(file.newDataInputStream())
        new ObjectMapper().writeValueAsString(model);
    }

    String toScriptTag() {
        String key = NameUtils.underscoreSeparate(NameUtils.filename(file.getName())).toUpperCase()
        return "<script>\nvar CONF_${key} = ${toJson()}\n</script>"
    }
}
