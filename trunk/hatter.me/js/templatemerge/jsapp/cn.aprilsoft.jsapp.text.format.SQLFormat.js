 /*****************************************************************************
 * This software is provided 'as-is', without any express or implied warranty.
 * In no event will the authors be held liable for any damages arising from
 * the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it freely,
 * subject to the following restrictions:
 *
 * 1. The origin of this software must not be misrepresented; you must not claim
 * that you wrote the original software. If you use this software in a product,
 * an acknowledgment in the product documentation would be appreciated but is
 * not required.
 *
 * 2. Altered source versions must be plainly marked as such, and must not be
 * misrepresented as being the original software.
 *
 * 3. This notice may not be removed or altered from any source distribution.
 *
 * The Original Code is: AnySoft Informatica
 *                       Marcelo Leite (aka Mr. Milk)
 *                       2005-12-11 mrmilk@anysoft.com.br
 *
 * The Initial Developer of the Original Code is AnySoft Informatica Ltda.
 * Portions created by AnySoft are Copyright (C) 2005 AnySoft Informatica Ltda
 * All Rights Reserved.
 *
 ********************************************************************************/
/*
 * cn.aprilsoft.jsapp.text.format.SQLFormat.js
 * jsapp, String sql format functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.text.format
  Package("cn.aprilsoft.jsapp.text.format");

  Using("cn.aprilsoft.jsapp.text.format.Format");

  Class("cn.aprilsoft.jsapp.text.format.SQLFormat", Extend("cn.aprilsoft.jsapp.text.format.Format"),
                                            Implement(),
  {
    KEY_WORDS: Static([
    "COUNT",                     "FETCH",                     "PRIMARY",
    "AVG",                       "FLOAT",                     "PROCEDURE",
    "MAX",                       "FOR",                       "PURGE",
    "MIN",                       "FORCE",                     "READ",
    "VAR",                       "FOREIGN",                   "READS",
    "STD",                       "FROM",                      "REAL",
    "CONCAT",                    "FULLTEXT",                  "REFERENCES",
    "ADD",                       "GOTO",                      "REGEXP",
    "ALL",                       "GRANT",                     "RENAME",
    "ALTER",                     "GROUP",                     "REPEAT",
    "ANALYZE",                   "HAVING",                    "REPLACE",
    "AND",                       "HIGH_PRIORITY",             "REQUIRE",
    "AS",                        "HOUR_MICROSECOND",          "RESTRICT",
    "ASC",                       "HOUR_MINUTE",               "RETURN",
    "ASENSITIVE",                "HOUR_SECOND",               "REVOKE",
    "BEFORE",                    "IF",                        "RIGHT",
    "BETWEEN",                   "IGNORE",                    "RLIKE",
    "BIGINT",                    "IN",                        "SCHEMA",
    "BINARY",                    "INDEX",                     "SCHEMAS",
    "BLOB",                      "INFILE",                    "SECOND_MICROSECOND",
    "BOTH",                      "INNER",                     "SELECT",
    "BY",                        "INOUT",                     "SENSITIVE",
    "CALL",                      "INSENSITIVE",               "SEPARATOR",
    "CASCADE",                   "INSERT",                    "SET",
    "CASE",                      "INT",                       "SHOW",
    "CHANGE",                    "INTEGER",                   "SMALLINT",
    "CHAR",                      "INTERVAL",                  "SONAME",
    "CHARACTER",                 "INTO",                      "SPATIAL",
    "CHECK",                     "IS",                        "SPECIFIC",
    "COLLATE",                   "ITERATE",                   "SQL",
    "COLUMN",                    "JOIN",                      "SQLEXCEPTION",
    "CONDITION",                 "KEY",                       "SQLSTATE",
    "CONNECTION",                "KEYS",                      "SQLWARNING",
    "CONSTRAINT",                "KILL",                      "SQL_BIG_RESULT",
    "CONTINUE",                  "LEADING",                   "SQL_CALC_FOUND_ROWS",
    "CONVERT",                   "LEAVE",                     "SQL_SMALL_RESULT",
    "CREATE",                    "LEFT",                      "SSL",
    "CROSS",                     "LIKE",                      "STARTING",
    "CURRENT_DATE",              "LIMIT",                     "STRAIGHT_JOIN",
    "CURRENT_TIME",              "LINES",                     "TABLE",
    "CURRENT_TIMESTAMP",         "LOAD",                      "TERMINATED",
    "CURRENT_USER",              "LOCALTIME",                 "THEN",
    "CURSOR",                    "LOCALTIMESTAMP",            "TINYBLOB",
    "DATABASE",                  "LOCK",                      "TINYINT",
    "DATABASES",                 "LONG",                      "TINYTEXT",
    "DAY_HOUR",                  "LONGBLOB",                  "TO",
    "DAY_MICROSECOND",           "LONGTEXT",                  "TRAILING",
    "DAY_MINUTE",                "LOOP",                      "TRIGGER",
    "DAY_SECOND",                "LOW_PRIORITY",              "TRUE",
    "DEC",                       "MATCH",                     "UNDO",
    "DECIMAL",                   "MEDIUMBLOB",                "UNION",
    "DECLARE",                   "MEDIUMINT",                 "UNIQUE",
    "DEFAULT",                   "MEDIUMTEXT",                "UNLOCK",
    "DELAYED",                   "MIDDLEINT",                 "UNSIGNED",
    "DELETE",                    "MINUTE_MICROSECOND",        "UPDATE",
    "DESC",                      "MINUTE_SECOND",             "USAGE",
    "DESCRIBE",                  "MOD",                       "USE",
    "DETERMINISTIC",             "MODIFIES",                  "USING",
    "DISTINCT",                  "NATURAL",                   "UTC_DATE",
    "DISTINCTROW",               "NOT",                       "UTC_TIME",
    "DIV",                       "NO_WRITE_TO_BINLOG",        "UTC_TIMESTAMP",
    "DOUBLE",                    "NULL",                      "VALUES",
    "DROP",                      "NUMERIC",                   "VARBINARY",
    "DUAL",                      "ON",                        "VARCHAR",
    "EACH",                      "OPTIMIZE",                  "VARCHARACTER",
    "ELSE",                      "OPTION",                    "VARYING",
    "ELSEIF",                    "OPTIONALLY",                "WHEN",
    "ENCLOSED",                  "OR",                        "WHERE",
    "ESCAPED",                   "ORDER",                     "WHILE",
    "EXISTS",                    "OUT",                       "WITH",
    "EXIT",                      "OUTER",                     "WRITE",
    "EXPLAIN",                   "OUTFILE",                   "XOR",
    "FALSE",                     "PRECISION",                 "YEAR_MONTH",
                                                              "ZEROFILL"]),
    
    Constructor: function()
    {
    },
    
    doFormat: function(sql)
    {
      var keywords = ThisClass().KEY_WORDS;
      if(sql == null)
      {
        throw new Exception("SQL cannot be null.");
      }
      sql = sql.replace(/\s+/ig, " ");
      for(var i = 0; i < keywords.length; i++)
      {
        rex = new RegExp("\\b" + keywords[i] + "\\b", "ig");
        sql = sql.replace(rex, keywords[i]);
      }
        
      sql = sql.replace(/\s*\b(UNION ALL|UNION|SELECT DISTINCT|SELECT|UPDATE|INSERT|DELETE|FROM|LEFT JOIN|RIGHT JOIN|INNER JOIN|WHERE|HAVING|ORDER BY|GROUP BY|LIMIT)\b\s*/ig, "\n$1\n\t");
      sql = sql.replace(/\s*\b(ON)\b\s*/ig, "\n\t$1\n\t\t");

      if(indent)
      {
        var indent = 0;
        var pos = 0;
        while(pos < sql.length)
        {
          var tab = "";
          var open  = sql.indexOf("(", pos);
          var close = sql.indexOf(")", pos);
          
          if((open < close) && (open != -1))
          {
            pos = open;
            indent++;
            for(var i = 0; i <= indent; i++)
              tab += "\t";
            var sql_a = sql.substr(0, pos);
            var sql_b = "(\n" + tab + sql.substr(pos + 1);
            sql = sql_a + sql_b;
            pos += tab.length + 2;
          }
          else if(close != -1)
          {
            pos = close;
            for(var i = 0; i < indent; i++)
            {
              tab += "\t";
            }
            var sql_a = sql.substr(0, pos);
            var sql_b = "\n" + tab + ")" + sql.substr(pos+1);
            sql = sql_a + sql_b;
            pos += tab.length + 2;
            indent--;
          }
          else
          {
            break;
          }
        }
        sql = sql.replace(/(\t+)([^\n]*)((?:,)\s*)/mig, "$1$2$3\n$1");
        sql = sql.replace(/([^)]\s*)(,(?!\s*\n)\s*)/ig,  "$1$2\n\t");
      }
      else
      {
        sql = sql.replace(/\s*(,)\s*/ig,  "$1\n\t");
      }
      
      sql = sql.replace(/(\t+)([^\n]*)(\b(?:OR)\s+)/mig, "$1$2\n$1$3\n$1\t");
      sql = sql.replace(/(\t+)([^\n]*)(\b(?:AND|THEN|ELSE)\s*\()/mig, "$1$2\n$1$3");
      while(sql.search(/(\t+)([^\t]+)(?=((AND|THEN|ELSE)\b\s+)(.+)).*/mig) != -1)
      {
        sql = sql.replace(/(\t+)([^\t]+)(?=((?:AND|THEN|ELSE)\b\s+)(.+)).*/mig, "$1$2\n$1$3\n$1\t$4");
      }
      
      return sql;
    }
  });
})();

