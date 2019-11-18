package com.workshop.sudokubook.jpa

import org.hibernate.dialect.MySQL55Dialect

/**
 * Allows use of HSQLDB MySQL mode, *with* hbm2ddl.auto
 */
class HsqldbMysql5Dialect : MySQL55Dialect() {
    override fun getTableTypeString(): String {
        return "" // HSQLDB doesn't like the 'engine=blah" syntax.
    }
}