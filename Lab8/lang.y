%{
#include <stdio.h>
#include <stdlib.h>

#define YYDEBUG 1 
%}

%token POUND
%token LET
%token BOOL
%token INT
%token STRING
%token READ
%token WRITE
%token IF
%token THEN
%token ELSE
%token FOR
%token WHILE
%token DO
%token NOT
%token AND
%token OR
%token ID
%token CONST
%token SEMICOLON
%token OPEN_CURLY
%token CLOSED_CURLY
%token OPEN_PARAN
%token CLOSED_PARAN
%token OPEN_SQUARE
%token CLOSED_SQUARE
%token ADD
%token SUB
%token MUL
%token DIV
%token MOD
%token LT
%token LE
%token EQ
%token NE
%token GE
%token GT
%token ATRIB

%start program 

%%

program : POUND compoundStatement POUND
statement : declarationStatement SEMICOLON | assignmentStatement SEMICOLON | ioStatement SEMICOLON | ifStatement SEMICOLON | loopStatement SEMICOLON
compoundStatement : statement optionalStatement
optionalStatement : compoundStatement |
declarationStatement : LET type declaree
type : INT | BOOL | STRING
declaree : ID arraySizeDeclaration
arraySizeDeclaration : OPEN_SQUARE CONST CLOSED_SQUARE |
assignmentStatement : address ATRIB expression
address : ID subscription
subscription : OPEN_SQUARE expression CLOSED_SQUARE |
expression : term secondExpression
secondExpression : ADD term secondExpression | SUB term secondExpression | OR term secondExpression |
term : factor secondTerm
secondTerm : MUL factor secondTerm | DIV factor secondTerm | MOD factor secondTerm | AND factor secondTerm |
factor : SUB factor | NOT factor | OPEN_PARAN expression CLOSED_PARAN  | relational | address | CONST
relational : OPEN_SQUARE expression comparator expression CLOSED_SQUARE
comparator : LT | LE | EQ | NE | GE | GT
ioStatement : readStatement | writeStatement
readStatement : READ OPEN_PARAN address CLOSED_PARAN
writeStatement : WRITE OPEN_PARAN expression CLOSED_PARAN
ifStatement : IF OPEN_PARAN expression CLOSED_PARAN THEN OPEN_CURLY compoundStatement CLOSED_CURLY elseBlock
elseBlock : ELSE OPEN_CURLY compoundStatement CLOSED_CURLY |
loopStatement : whileStatement | forStatement
forStatement : FOR OPEN_PARAN assignmentStatement SEMICOLON expression SEMICOLON assignmentStatement CLOSED_PARAN DO OPEN_CURLY compoundStatement CLOSED_CURLY
whileStatement : WHILE OPEN_PARAN expression CLOSED_PARAN DO OPEN_CURLY compoundStatement CLOSED_CURLY

%%

yyerror(char *s)
{	
	printf("%s\n",s);
}

extern FILE *yyin;

main(int argc, char **argv)
{
	if(argc>1) yyin :  fopen(argv[1],"r");
	if(argc>2 && !strcmp(argv[2],"-d")) yydebug: 1;
	if(!yyparse()) fprintf(stderr, "\tO.K.\n");
}

