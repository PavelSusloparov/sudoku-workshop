package com.workshop.sudokubook.cucumber

import cucumber.api.TypeRegistryConfigurer
import cucumber.api.TypeRegistry
import io.cucumber.cucumberexpressions.ParameterByTypeTransformer
import io.cucumber.datatable.TableCellByTypeTransformer
import io.cucumber.datatable.TableEntryByTypeTransformer
import com.fasterxml.jackson.databind.ObjectMapper

import java.lang.reflect.Type
import java.util.Locale

import java.util.Locale.ENGLISH

class TypeRegistryConfiguration : TypeRegistryConfigurer {

    override fun locale(): Locale {
        return ENGLISH
    }

    override fun configureTypeRegistry(typeRegistry: TypeRegistry) {
        val transformer = Transformer()
        typeRegistry.setDefaultDataTableCellTransformer(transformer)
        typeRegistry.setDefaultDataTableEntryTransformer(transformer)
        typeRegistry.setDefaultParameterTransformer(transformer)
    }

    class Transformer : ParameterByTypeTransformer, TableEntryByTypeTransformer, TableCellByTypeTransformer {
        private val objectMapper: ObjectMapper = ObjectMapper()

        override fun transform(s: String, type: Type) {
            return objectMapper.convertValue(s, objectMapper.constructType(type))
        }

        @Throws(Throwable::class)
        override fun <T> transform(entry: Map<String, String>, aClass: Class<T>, cellTransformer: TableCellByTypeTransformer): T {
            return objectMapper.convertValue(entry, aClass)
        }

        @Throws(Throwable::class)
        override fun <T> transform(value: String, cellType: Class<T>): T {
            return objectMapper.convertValue(value, cellType)
        }
    }
}
