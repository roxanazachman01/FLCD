%option noyywrap
%{
#include <stdio.h>
%}

identifier (_|[a-zA-Z])([a-zA-Z]|[0-9])*
integer (0|([-+]?([1-9][0-9]*)))
string [a-zA-Z ]*
constchar "\'"[a-zA-Z ]"\'"
conststring \"{string}\"
separator " "|"("|")"|"["|"]"|"{"|"}"|";"|"\n"|"\t"|"\r"
whitespace " "|"\n"|"\t"|"\r"
operator "+"|"-"|"*"|"/"|"<="|"<"|">="|">"|"!="|"=="|"="|"%"
reserved "char"|"string"|"int"|"const"|"if"|"else"|"read"|"write"|"while"|"begin"|"end"
invalid_id {integer}{identifier}
invalid_string (\'{string}\')|(\'{string})|({string}\')|(\"{string})|({string}\")
invalid_integer ([-+]?0{integer}|[-+]0)
error ({invalid_id}|{invalid_string}|{invalid_integer}|.)

%%

{whitespace} ;
{separator} printf("Separator: %s\n",yytext);
{operator}  printf("Operator: %s\n",yytext);
{reserved} printf( "A reserved word: %s\n", yytext);  
{identifier} printf("Identifier: %s\n",yytext);
{integer} printf("Constant number: %s\n",yytext);
{constchar} printf("Constant char: %s\n",yytext);
{conststring} printf("Constant string: %s\n",yytext);
{error} printf("Error: %s\n", yytext);

%%

int main( argc, argv ) 
int argc; 
char **argv; 
{ 
    ++argv, --argc;
    if ( argc > 0 ) 
    yyin = fopen( argv[0], "r" ); 
    else 
     yyin = stdin; 
    yylex(); 
} 