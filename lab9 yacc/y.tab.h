/* A Bison parser, made by GNU Bison 3.5.1.  */

/* Bison interface for Yacc-like parsers in C

   Copyright (C) 1984, 1989-1990, 2000-2015, 2018-2020 Free Software Foundation,
   Inc.

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* Undocumented macros, especially those whose name start with YY_,
   are private implementation details.  Do not rely on them.  */

#ifndef YY_YY_Y_TAB_H_INCLUDED
# define YY_YY_Y_TAB_H_INCLUDED
/* Debug traces.  */
#ifndef YYDEBUG
# define YYDEBUG 0
#endif
#if YYDEBUG
extern int yydebug;
#endif

/* Token type.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
  enum yytokentype
  {
    CHAR = 258,
    STRING = 259,
    INT = 260,
    IF = 261,
    ELSE = 262,
    READ = 263,
    WRITE = 264,
    WHILE = 265,
    BEGINN = 266,
    END = 267,
    ROP = 268,
    RCP = 269,
    SOB = 270,
    SCB = 271,
    COB = 272,
    CCB = 273,
    COLON = 274,
    PLUS = 275,
    MINUS = 276,
    MULTIPLY = 277,
    DIV = 278,
    LE = 279,
    LT = 280,
    GE = 281,
    GT = 282,
    NE = 283,
    EQ = 284,
    ASSIGN = 285,
    MOD = 286,
    IDENTIFIER = 287,
    CONSTNR = 288,
    CONSTCHAR = 289,
    CONSTSTRING = 290
  };
#endif
/* Tokens.  */
#define CHAR 258
#define STRING 259
#define INT 260
#define IF 261
#define ELSE 262
#define READ 263
#define WRITE 264
#define WHILE 265
#define BEGINN 266
#define END 267
#define ROP 268
#define RCP 269
#define SOB 270
#define SCB 271
#define COB 272
#define CCB 273
#define COLON 274
#define PLUS 275
#define MINUS 276
#define MULTIPLY 277
#define DIV 278
#define LE 279
#define LT 280
#define GE 281
#define GT 282
#define NE 283
#define EQ 284
#define ASSIGN 285
#define MOD 286
#define IDENTIFIER 287
#define CONSTNR 288
#define CONSTCHAR 289
#define CONSTSTRING 290

/* Value type.  */
#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef int YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define YYSTYPE_IS_DECLARED 1
#endif


extern YYSTYPE yylval;

int yyparse (void);

#endif /* !YY_YY_Y_TAB_H_INCLUDED  */
