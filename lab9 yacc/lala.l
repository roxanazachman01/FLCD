%{
#include "y.tab.h"
#include <stdlib.h>
#include <stdio.h>
%}

%option noyywrap
%option caseless

identifier (_|[a-zA-Z])([a-zA-Z]|[0-9])*
constnr (-)?([1-9]+[0-9]*)|0
char [a-zA-Z0-9 ]
constchar "\'"{char}"\'"
conststring \"{char}*\"
separator " "|"("|")"|"["|"]"|"{"|"}"|";"|":"|"\n"|"\t"|"\r"
operator "+"|"-"|"*"|"/"|"//"|"<="|"<"|">="|">"|"!="|"=="|"="|"%"
reserved "char"|"string"|"int"|"const"|"if"|"else"|"read"|"write"|"while"

%%

[-+]0 printf("Error: %s\n", yytext);
[-+]?0{constnr} printf("Error: %s\n", yytext);
{constnr}{identifier} printf("Error: %s\n", yytext);

"\r"|" "|"\n"|"\t" ;

"(" {printf("%s\n",yytext); return OPEN_ROUND_BRACKET;}
")" {printf("%s\n",yytext); return CLOSED_ROUND_BRACKET;}
"[" {printf("%s\n",yytext); return OPEN_SQUARE_BRACKET;}
"]" {printf("%s\n",yytext); return CLOSED_SQUARE_BRACKET;}
"{" {printf("%s\n",yytext); return OPEN_CURLY_BRACKET;}
"}" {printf("%s\n",yytext); return CLOSED_CURLY_BRACKET;}
";" {printf("%s\n",yytext); return SEMICOLON;}
"<=" {printf("%s\n",yytext); return LTE;}
">=" {printf("%s\n",yytext); return GTE;}
"==" {printf("%s\n",yytext); return EQUAL;}
"!=" {printf("%s\n",yytext); return NOT_EQUAL;}
"+" {printf("%s\n",yytext); return PLUS;}
"-" {printf("%s\n",yytext); return MINUS;}
"*" {printf("%s\n",yytext); return MULT;}
"/" {printf("%s\n",yytext); return DIV;}
"<" {printf("%s\n",yytext); return LT;}
">" {printf("%s\n",yytext); return GT;}
"=" {printf("%s\n",yytext); return ASSIGN;}

"char" {printf("%s\n",yytext); return CHAR;}
"string" {printf("%s\n",yytext); return STRING;}
"int" {printf("%s\n",yytext); return INT;}
"if" {printf("%s\n",yytext); return IF;}
"else" {printf("%s\n",yytext); return ELSE;}
"read" {printf("%s\n",yytext); return READ;}
"write" {printf("%s\n",yytext); return WRITE;}
"while" {printf("%s\n",yytext); return WHILE;}

{separator} printf("Separator: %s\n",yytext);
{reserved} printf( "A reserved word: %s\n", yytext);  

{identifier} {printf("%s\n",yytext); return IDENTIFIER;}
{constnr} {printf("%s\n",yytext); return INTEGER;}
{constchar} {printf("%s\n",yytext); return CONSTCHAR;}
{conststring} {printf("%s\n",yytext); return CONSTSTRING;}

(\'{char}\')|(\'{char})|({char}\')|(\"{char})|({char}*\") printf("Error: %s\n", yytext);
. printf("Error: '%s'\n", yytext);

%%