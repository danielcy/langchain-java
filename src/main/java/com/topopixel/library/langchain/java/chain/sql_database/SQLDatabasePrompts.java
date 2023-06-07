package com.topopixel.library.langchain.java.chain.sql_database;

import com.topopixel.library.langchain.java.prompts.PromptTemplate;
import java.util.Arrays;

public class SQLDatabasePrompts {

    private static final String PROMPT_SUFFIX = "Only use the following tables:\n" +
    "{table_info}\n" +
    "\n" +
    "Question: {input}";

    private static final String _mysql_prompt = "You are a MySQL expert. Given an input question, first create a syntactically correct MySQL query to run, then look at the results of the query and return the answer to the input question.\n" +
        "Unless the user specifies in the question a specific number of examples to obtain, query for at most {top_k} results using the LIMIT clause as per MySQL. You can order the results to return the most informative data in the database.\n" +
        "Never query for all columns from a table. You must query only the columns that are needed to answer the question. Wrap each column name in backticks (`) to denote them as delimited identifiers.\n" +
        "Pay attention to use only the column names you can see in the tables below. Be careful to not query for columns that do not exist. Also, pay attention to which column is in which table.\n" +
        "Pay attention to use CURDATE() function to get the current date, if the question involves \"today\"."
        + "\n"
        + "Use the following format:\n"
        + "\n"
        + "Question: Question here\n"
        + "SQLQuery: SQL Query to run\n"
        + "SQLResult: Result of the SQLQuery\n"
        + "Answer: Final answer here";

    public static final PromptTemplate MYSQL_PROMPT = new PromptTemplate(
        Arrays.asList("input", "table_info", "top_k"),
        _mysql_prompt + PROMPT_SUFFIX);

    // TODO: other sql prompts
}
