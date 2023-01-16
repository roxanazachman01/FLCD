%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define YYDEBUG 1

int yylex();
void yyerror();
int indexes[5000],n=0;
void add_index(int index){
	indexes[n]=index;
	n++;
}

void print_indexes(){
	for(int i=0;i<n;i++)
		printf("%d ",indexes[i]);
}
%}

%token CHAR STRING INT IF ELSE READ WRITE WHILE BEGINN END
%token ROP RCP SOB SCB COB CCB COLON
%token PLUS MINUS MULTIPLY DIV LE LT GE GT NE EQ ASSIGN MOD

%token IDENTIFIER
%token CONSTNR
%token CONSTCHAR
%token CONSTSTRING

%%

program: COB decllist cmpdstmt CCB
		;
		
decllist: declaration decllistlala
		;
decllistlala: decllist 
		|
		;
declaration: type IDENTIFIER COLON
		;
type: type1 arraydecl
		;
type1: INT
		| CHAR
		| STRING
		;
arraydecl: CONSTNR SCB
		|
		;
cmpdstmt: BEGINN stmtlist END
		;
stmtlist: stmt stmtlistlala
		;
stmtlistlala: stmtlist
		|
;
stmt: simplstmt COLON
		| structstmt
;
simplstmt: assignstmt
		| iostmt
;
assignstmt: IDENTIFIER assignstmtlala ASSIGN expression
;
assignstmtlala: SOB assignstmtlala2 SCB
		|
;
assignstmtlala2: CONSTNR
		| IDENTIFIER 
;
expression: term expression1 
;
expression1: PLUS expression1 
		| MINUS expression1 
		| expression 
		|
;
term: factor term1 
;
term1: MULTIPLY term1 
		| MOD term1
		| DIV term1
		|
;
factor: ROP expression RCP 
		| IDENTIFIER assignstmtlala 
		| CONSTNR 
;
iostmt: READ ROP IDENTIFIER assignstmtlala RCP 
		| WRITE ROP writeitem RCP 
;
writeitem: lalaitem
		| IDENTIFIER assignstmtlala
;
lalaitem: CONSTNR
		| CONSTCHAR
		| CONSTSTRING
;
structstmt: ifstmt
		| whilestmt
;
ifstmt: IF ROP condition RCP COB stmtlist CCB elsestmt
;
elsestmt: ELSE COB stmtlist CCB
		| 
;
whilestmt: WHILE ROP condition RCP COB stmtlist CCB
;
condition: expression relation expression 
;
relation: LT
		| LE
		| GE
		| GT
		| EQ
		| NE
;

%%

void yyerror(char *s)
{
  printf("%s\n", s);
}

extern FILE *yyin;

int main(int argc, char **argv)
{
  if(argc>1) yyin = fopen(argv[1], "r");
  if((argc>2)&&(!strcmp(argv[2],"-d"))) yydebug = 1;
  if(!yyparse()) {
	fprintf(stderr,"Parsed with success.\n");
	print_indexes();
  }
}

