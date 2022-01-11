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
    POUND = 258,
    LET = 259,
    BOOL = 260,
    INT = 261,
    STRING = 262,
    READ = 263,
    WRITE = 264,
    IF = 265,
    THEN = 266,
    ELSE = 267,
    FOR = 268,
    WHILE = 269,
    DO = 270,
    NOT = 271,
    AND = 272,
    OR = 273,
    ID = 274,
    CONST = 275,
    SEMICOLON = 276,
    OPEN_CURLY = 277,
    CLOSED_CURLY = 278,
    OPEN_PARAN = 279,
    CLOSED_PARAN = 280,
    OPEN_SQUARE = 281,
    CLOSED_SQUARE = 282,
    ADD = 283,
    SUB = 284,
    MUL = 285,
    DIV = 286,
    MOD = 287,
    LT = 288,
    LE = 289,
    EQ = 290,
    NE = 291,
    GE = 292,
    GT = 293,
    ATRIB = 294
  };
#endif
/* Tokens.  */
#define POUND 258
#define LET 259
#define BOOL 260
#define INT 261
#define STRING 262
#define READ 263
#define WRITE 264
#define IF 265
#define THEN 266
#define ELSE 267
#define FOR 268
#define WHILE 269
#define DO 270
#define NOT 271
#define AND 272
#define OR 273
#define ID 274
#define CONST 275
#define SEMICOLON 276
#define OPEN_CURLY 277
#define CLOSED_CURLY 278
#define OPEN_PARAN 279
#define CLOSED_PARAN 280
#define OPEN_SQUARE 281
#define CLOSED_SQUARE 282
#define ADD 283
#define SUB 284
#define MUL 285
#define DIV 286
#define MOD 287
#define LT 288
#define LE 289
#define EQ 290
#define NE 291
#define GE 292
#define GT 293
#define ATRIB 294

/* Value type.  */
#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef int YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define YYSTYPE_IS_DECLARED 1
#endif


extern YYSTYPE yylval;

int yyparse (void);

#endif /* !YY_YY_Y_TAB_H_INCLUDED  */
