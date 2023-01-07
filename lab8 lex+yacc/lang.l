%option noyywrap
%{
#include <stdio.h>
%}

identifier (_|[a-zA-Z])([a-zA-Z]|[0-9])*
constnr (0|([-+]?([1-9][0-9]*)))
char [a-zA-Z ]
constchar "\'"{char}"\'"
conststring \"{char}*\"
separator " "|"("|")"|"["|"]"|"{"|"}"|";"|"\n"|"\t"|"\r"
operator "+"|"-"|"*"|"/"|"<="|"<"|">="|">"|"!="|"=="|"="|"%"
reserved "char"|"string"|"int"|"const"|"if"|"else"|"read"|"write"|"while"|"begin"|"end"

%%

[-+]?0{constnr} printf("Error: %s\n", yytext);
{constnr}{identifier} printf("Error: %s\n", yytext);
{separator} {
if(*yytext!='\r' && *yytext!=' ' && *yytext!='\n' && *yytext!='\t')
printf("Separator: %s\n",yytext);
}
{operator}  printf("Operator: %s\n",yytext);
{reserved} printf( "A reserved word: %s\n", yytext);  
{identifier} printf("Identifier: %s\n",yytext);
{constnr} printf("Constant number: %s\n",yytext);
{constchar} printf("Constant char: %s\n",yytext);
{conststring} printf("Constant string: %s\n",yytext);
(\'{char}*\')|(\'{char}*)|({char}*\')|(\"{char}*)|({char}*\") printf("Error: %s\n", yytext);

. printf("Error: '%s'\n", yytext);

%%
int main( argc, argv ) 
int argc; 
char **argv; 
{ 
    ++argv, --argc; /* skip over program name */ 
    if ( argc > 0 ) 
    yyin = fopen( argv[0], "r" ); 
    else 
     yyin = stdin; 
    yylex(); 
} 