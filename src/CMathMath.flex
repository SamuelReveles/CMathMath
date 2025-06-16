import java_cup.runtime.Symbol;

%%
%class CMathMathLexer
%unicode
%public
%column
%line
%cup

WHITESPACE = [\s\n]+
IMAGINARY = [0-9]+(\.[0-9]+)?i
REAL = [0-9]+(\.[0-9]+)?
DEGREES = [0-9]+(\.[0-9]+)?°
IDENTIFIER = [a-zA-Z_][a-zA-Z0-9_]*
STRING = \"(\\.|[^\"\\])*\"
COMMENT = "//".*
ERROR = .
%%

{WHITESPACE} { /*Ignorar lol*/ }
{COMMENT} { /*Ignorar lol*/ }
{IMAGINARY} {  return new Symbol(sym.IMAGINARY, yyline, yycolumn, Double.parseDouble(yytext().substring(0, yytext().length() - 1))); }
{DEGREES} { return new Symbol(sym.DEGREES, yyline, yycolumn, Double.parseDouble(yytext().substring(0, yytext().length() - 1))); }
{STRING} { return new Symbol(sym.STRING, yyline, yycolumn, yytext().substring(1, yytext().length()-1)); }
{REAL} {  return new Symbol(sym.REAL, yyline, yycolumn, Double.parseDouble(yytext())); }
"+" { return new Symbol(sym.PLUS, yyline, yycolumn, yytext()); }
"-" { return new Symbol(sym.MINUS, yyline, yycolumn, yytext()); }
"*" { return new Symbol(sym.MULTIPLICATION, yyline, yycolumn, yytext()); }
"^" { return new Symbol(sym.POW, yyline, yycolumn, yytext()); }
"=" { return new Symbol(sym.EQUALS, yyline, yycolumn, yytext()); }
"/" { return new Symbol(sym.DIVISION, yyline, yycolumn, yytext()); }
"(" { return new Symbol(sym.LEFT_PARENTH, yyline, yycolumn, yytext()); }
")" { return new Symbol(sym.RIGHT_PARENTH, yyline, yycolumn, yytext()); }
"{" { return new Symbol(sym.LEFT_BRACE, yyline, yycolumn, yytext()); }
"}" { return new Symbol(sym.RIGHT_BRACE, yyline, yycolumn, yytext()); }
"," { return new Symbol(sym.COMMA, yyline, yycolumn, yytext()); }
"sen" { return new Symbol(sym.SIN, yyline, yycolumn, yytext()); }
"cos" { return new Symbol(sym.COS, yyline, yycolumn, yytext()); }
"tan" { return new Symbol(sym.TAN, yyline, yycolumn, yytext()); }
"sec" { return new Symbol(sym.SEC, yyline, yycolumn, yytext()); }
"csc" { return new Symbol(sym.CSC, yyline, yycolumn, yytext()); }
"cot" { return new Symbol(sym.COT, yyline, yycolumn, yytext()); }
"union" { return new Symbol(sym.UNION, yyline, yycolumn, yytext()); }
"interseccion" { return new Symbol(sym.INTERSECT, yyline, yycolumn, yytext()); }
"redondear" { return new Symbol(sym.ROUND, yyline, yycolumn, yytext()); }
"dado" { return new Symbol(sym.DADO, yyline, yycolumn, yytext()); }
"equivale" { return new Symbol(sym.EQUIVALE, yyline, yycolumn, yytext()); }
"si" { return new Symbol(sym.SI, yyline, yycolumn, yytext()); }
"si no" { return new Symbol(sym.SINO, yyline, yycolumn, yytext()); }
"pero si" { return new Symbol(sym.PEROSI, yyline, yycolumn, yytext()); }
"mientras" { return new Symbol(sym.MIENTRAS, yyline, yycolumn, yytext()); }
"desde" { return new Symbol(sym.DESDE, yyline, yycolumn, yytext()); }
"con" { return new Symbol(sym.CON, yyline, yycolumn, yytext()); }
"avanzar" { return new Symbol(sym.AVANZAR, yyline, yycolumn, yytext()); }
"tal que" { return new Symbol(sym.TALQUE, yyline, yycolumn, yytext()); }
"conjunto" { return new Symbol(sym.CONJUNTO, yyline, yycolumn, yytext()); }
"PI" { return new Symbol(sym.PI, yyline, yycolumn, yytext()); }
"imprimir" { return new Symbol(sym.PRINT, yyline, yycolumn, yytext()); }
{IDENTIFIER} { return new Symbol(sym.ID, yyline, yycolumn, yytext()); }
{ERROR} {  System.out.println("Entrada inválida en la línea " + yyline + ", "+ yycolumn + ": " + yytext()); }