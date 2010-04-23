// A complete lexer and grammar for CSS 2.1 as defined by the
// W3 specification.
//
// This grammar is free to use providing you retain everyhting in this header comment
// section.
//
// Author      : Jim Idle, Temporal Wave LLC.
// Contact     : jimi@temporal-wave.com
// Website     : http://www.temporal-wave.com
// License     : ANTLR Free BSD License
//
// Please visit our Web site at http://www.temporal-wave.com and try our commercial
// parsers for SQL, C#, VB.Net and more.
//
// This grammar is free to use providing you retain everything in this header comment
// section.
//
grammar LessCss;

options {
    output = AST;
}

tokens {
    RULESET;
    SELECTOR;
    DECLARATION;
    IMPORT;
    PROP_VALUE;
    LITERAL;
    EXPR;
    VAR;
    CONSTANT;
    FUNCTION;
}

@header {
package org.lesscss4j.parser;
}
@lexer::header {
package org.lesscss4j.parser;
}

// -------------
// Main rule.   This is the main entry rule for the parser, the top level
//              grammar rule.
//
// A style sheet consists of an optional character set specification, an optional series
// of imports, and then the main body of style rules.
//
styleSheet  
    : WS*
      (charSet WS!*)?
      (importFile WS!*)*
      (bodyset WS!*)*
      EOF!
    ;
    
// -----------------
// Character set.   Picks up the user specified character set, should it be present.
//
charSet
    : '@' CHARSET WS+ STRING WS* SEMI
    -> ^(CHARSET STRING)
    ;

// ---------
// Import.  Location of an external style sheet to include in the ruleset.
//
importFile
    : '@' IMPORT_SYM WS+ importLocation  (WS+ medium (WS* COMMA WS* medium)*)? WS* SEMI
    -> ^(IMPORT importLocation medium*)
    ;
    
importLocation : STRING|URI ;

// ---------
// Media.   Introduce a set of rules that are to be used if the consumer indicates
//          it belongs to the signified medium.
//
media
    : '@' MEDIA_SYM WS+ medium (WS* COMMA WS* medium)* WS*
        LBRACE WS*
            (ruleSet WS*)*
        RBRACE
    -> ^(MEDIA_SYM medium+ ruleSet*)
    ;

// ---------    
// Medium.  The name of a medim that are particulare set of rules applies to.
//
medium
    : ident 
    ;
    

bodylist
    : (bodyset WS!*)*
    ;
    
bodyset
    : variableDef
    | ruleSet
    | media
    | page
    ;   
    
page
    : '@' PAGE_SYM (WS+ COLON pseudoPage)? WS* LBRACE WS* (declaration WS*)* RBRACE
    -> ^(PAGE_SYM pseudoPage? declaration*)
    ;

pseudoPage
    : ident
    ;
    
combinator
    : WS* combinatorNonWs WS*
    | WS+
    ;
    
combinatorNonWs
    : PLUS
    | GREATER
    ;
            
ruleSet
    : selector (WS* COMMA WS* selector)* WS* 
      LBRACE 
        (WS* ruleSetElement)* 
      WS* RBRACE
    -> ^(RULESET ^(SELECTOR selector)+ ruleSetElement*)
    ;
    
ruleSetElement
    : declaration
    | variableDef
    ;

selector
    : simpleSelector (combinator simpleSelector)*
    ;

simpleSelector
    : elementName (elementSubsequent)*
    | (elementSubsequent)+
    ;

esPred
    : HASH | DOT | LBRACKET | COLON
    ;

elementSubsequent
    : HASH
    | cssClass
    | attrib
    | pseudo
    ;

cssClass
    : DOT ident
    ;

elementName
    : ident
    | STAR
    ;

attrib
    : LBRACKET
        ident
            (
                ( OPEQ | INCLUDES | DASHMATCH )
                ( ident | STRING )
            )?
      RBRACKET
;

pseudo
    : COLON COLON? ident ( LPAREN ident? RPAREN )?
    ;
    
variableDef
    : variable WS* COLON WS* propertyValue WS* SEMI
    -> ^(VAR variable ^(EXPR propertyValue))
    ;
    
variable
    : '@' ident -> ^(ident)
    ;

literal
    : (STRING | URI | ident)
    ;
    
additiveExpression
    : multiplicativeExpression ( WS!* (PLUS|MINUS)^ WS!* multiplicativeExpression )* 
    ;
    
multiplicativeExpression
    : primaryExpression ( WS!* (STAR|SOLIDUS)^ WS!* primaryExpression)*
    ;
    
primaryExpression
    : (LPAREN! WS!*) additiveExpression (WS!* RPAREN!)
    | exprValue
    ;

exprValue
    : variable      -> ^(VAR      variable)
    | numericValue  -> ^(CONSTANT numericValue)
    ;
    
numericValue
    : NUMBER
    | hexColor
    ;
    
declaration
    : property WS* COLON (WS* propertyValue (WS* important)?)? WS* SEMI
    -> ^(DECLARATION property ^(PROP_VALUE propertyValue)? important?)
    ;

property
    : STAR? ident
    -> ^(ident STAR?)
    ;
    
propertyValue
    : propertyTerm ((WS* COMMA WS*|WS+) propertyTerm)*
    ;
    
propertyTerm
    : (ident WS* LPAREN)=>function
    | literal              -> ^(LITERAL literal)
    | additiveExpression   -> ^(EXPR additiveExpression)
    ;

// Internet Explorer specific functions
function
    : (ALPHA)=>ieAlpha
    | (ident WS* LPAREN ieExprTerm RPAREN -> ^(FUNCTION ident ieExprTerm))
    ;

ieAlpha
    : ALPHA WS* LPAREN WS* ieAlphaTerm (WS* COMMA WS* ieAlphaTerm)* WS* RPAREN
    -> ^(FUNCTION ALPHA ieAlphaTerm*)
    ;
    
ieAlphaTerm
    : ident WS* OPEQ WS* (literal | additiveExpression)
    -> ^(OPEQ ident ^(LITERAL literal)? ^(EXPR additiveExpression)?)
    ;

ieExprTerm
    : (STRING|'&'|'?'|~('&'|'?'|'('|')'|STRING))*
      ( LPAREN ieExprTerm RPAREN (STRING | ~('('|')'|STRING))* )*
    ;

important
    : IMPORTANT_SYM
    ;
/*    
expr 
    : term ((WS+|WS* operator WS*) term)*
    ;

term
    : additiveExpression
    | unaryOperator?
        (
              NUMBER
            | PERCENTAGE
            | LENGTH
            | EMS
            | EXS
            | ANGLE
            | TIME
            | FREQ
        )
    | STRING
    | function
    | IDENT
    | URI
    | hexColor
    ;

unaryOperator
    : MINUS
    | PLUS
    ;  
    
 */   

hexColor 
    : HASH
    ;
    
ident    
    : IDENT 
    | ALPHA 
    | EXPRESSION 
    | CHARSET
    | MEDIA_SYM
    | IMPORT_SYM
    | PAGE_SYM
    ;

// ==============================================================
// LEXER
//
// The lexer follows the normative section of WWW standard as closely
// as it can. For instance, where the ANTLR lexer returns a token that
// is unambiguous for both ANTLR and lex (the standard defines tokens
// in lex notation), then the token names are equivalent.
//
// Note however that lex has a match order defined as top to bottom 
// with longest match first. This results in a fairly inefficent, match,
// REJECT, match REJECT set of operations. ANTLR lexer grammars are actaully
// LL grammars (and hence LL recognizers), which means that we must
// specifically disambiguate longest matches and so on, when the lex
// like normative grammar results in ambiguities as far as ANTLR is concerned.
//
// This means that some tokens will either be combined compared to the
// normative spec, and the paresr will recognize them for what they are.
// In this case, the token will named as XXX_YYY where XXX and YYY are the
// token names used in the specification.
//
// Lex style macro names used in the spec may sometimes be used (in upper case
// version) as fragment rules in this grammar. However ANTLR fragment rules
// are not quite the same as lex macros, in that they generate actual 
// methods in the recognizer class, and so may not be as effecient. In
// some cases then, the macro contents are embedded. Annotation indicate when
// this is the case.
//
// See comments in the rules for specific details.
// --------------------------------------------------------------
//
// N.B. CSS 2.1 is defined as case insensitive, but because each character
//      is allowed to be written as in escaped form we basically define each
//      character as a fragment and reuse it in all other rules.
// ==============================================================


// --------------------------------------------------------------
// Define all the fragments of the lexer. These rules neither recognize
// nor create tokens, but must be called from non-fragment rules, which
// do create tokens, using these fragments to either purely define the
// token number, or by calling them to match a certain portion of
// the token string.
//

fragment    HEXCHAR     : ('a'..'f'|'A'..'F'|'0'..'9')  ;

fragment    NONASCII    : '\u0080'..'\uFFFF'            ;   // NB: Upper bound should be \u4177777

fragment    UNICODE     : '\\' HEXCHAR 
                                (HEXCHAR 
                                    (HEXCHAR 
                                        (HEXCHAR 
                                            (HEXCHAR HEXCHAR?)?
                                        )?
                                    )?
                                )? 
                                ('\r'|'\n'|'\t'|'\f'|' ')*  ;
                                
fragment    ESCAPE      : UNICODE | '\\' ~('\r'|'\n'|'\f'|HEXCHAR)  ;

fragment    NMSTART     : '_'
                        | 'a'..'z'
                        | 'A'..'Z'
                        | NONASCII
                        | ESCAPE
                        ;

fragment    NMCHAR      : '_'
                        | 'a'..'z'
                        | 'A'..'Z'
                        | '0'..'9'
                        | '-'
                        | NONASCII
                        | ESCAPE
                        ;
                        
fragment    NAME        : NMCHAR+   ;

fragment    URL         : ( 
                              '['|'!'|'#'|'$'|'%'|'&'|'*'|'-'|'~'
                            | NONASCII
                            | ESCAPE
                          )*
                        ;

                        
// Basic Alpha characters in upper, lower and escaped form. Note that
// whitespace and newlines are unimportant even within keywords. We do not
// however call a further fragment rule to consume these characters for
// reasons of performance - the rules are still eminently readable.
//
fragment    A   :   ('a'|'A') | '\\' ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')'1' ;
fragment    B   :   ('b'|'B') | '\\' ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')'2' ;
fragment    C   :   ('c'|'C') | '\\' ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')'3' ;
fragment    D   :   ('d'|'D') | '\\' ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')'4' ;
fragment    E   :   ('e'|'E') | '\\' ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')'5' ;
fragment    F   :   ('f'|'F') | '\\' ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')'6' ;
fragment    G   :   ('g'|'G') | '\\' ( 'g' | 'G' | ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')'7' ) ;
fragment    H   :   ('h'|'H') | '\\' ( 'h' | 'H' | ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')'8' ) ;
fragment    I   :   ('i'|'I') | '\\' ( 'i' | 'I' | ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')'9' ) ;
fragment    J   :   ('j'|'J') | '\\' ( 'j' | 'J' | ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')('A'|'a') ) ;
fragment    K   :   ('k'|'K') | '\\' ( 'k' | 'K' | ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')('B'|'b') ) ;
fragment    L   :   ('l'|'L') | '\\' ( 'l' | 'L' | ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')('C'|'c') ) ;
fragment    M   :   ('m'|'M') | '\\' ( 'm' | 'M' | ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')('D'|'d') ) ;
fragment    N   :   ('n'|'N') | '\\' ( 'n' | 'N' | ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')('E'|'e') ) ;
fragment    O   :   ('o'|'O') | '\\' ( 'o' | 'O' | ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')('F'|'f') ) ;
fragment    P   :   ('p'|'P') | '\\' ( 'p' | 'P' | ('0' ('0' ('0' '0'?)?)?)? ('5'|'7')('0') ) ;
fragment    Q   :   ('q'|'Q') | '\\' ( 'q' | 'Q' | ('0' ('0' ('0' '0'?)?)?)? ('5'|'7')('1') ) ;
fragment    R   :   ('r'|'R') | '\\' ( 'r' | 'R' | ('0' ('0' ('0' '0'?)?)?)? ('5'|'7')('2') ) ;
fragment    S   :   ('s'|'S') | '\\' ( 's' | 'S' | ('0' ('0' ('0' '0'?)?)?)? ('5'|'7')('3') ) ;
fragment    T   :   ('t'|'T') | '\\' ( 't' | 'T' | ('0' ('0' ('0' '0'?)?)?)? ('5'|'7')('4') ) ;
fragment    U   :   ('u'|'U') | '\\' ( 'u' | 'U' | ('0' ('0' ('0' '0'?)?)?)? ('5'|'7')('5') ) ;
fragment    V   :   ('v'|'V') | '\\' ( 'v' | 'V' | ('0' ('0' ('0' '0'?)?)?)? ('5'|'7')('6') ) ;
fragment    W   :   ('w'|'W') | '\\' ( 'w' | 'W' | ('0' ('0' ('0' '0'?)?)?)? ('5'|'7')('7') ) ;
fragment    X   :   ('x'|'X') | '\\' ( 'x' | 'X' | ('0' ('0' ('0' '0'?)?)?)? ('5'|'7')('8') ) ;
fragment    Y   :   ('y'|'Y') | '\\' ( 'y' | 'Y' | ('0' ('0' ('0' '0'?)?)?)? ('5'|'7')('9') ) ;
fragment    Z   :   ('z'|'Z') | '\\' ( 'z' | 'Z' | ('0' ('0' ('0' '0'?)?)?)? ('5'|'7')('A'|'a') ) ;
fragment DIGIT  :  '0'..'9'
    ;
    

// -------------
// Comments.    Comments may not be nested, may be multilined and are delimited
//              like C comments: /* ..... */
//              LessCSS supports line comments too of the form //....
//              COMMENTS are hidden from the parser which simplifies the parser 
//              grammar a lot.
//
COMMENT         : ('/*' ( options { greedy=false; } : .*) '*/' | '/' '/' (~('\n'|'\r'|'\f'))*) 
                    { $channel = 2; }  // Comments on channel 2 in case we want to find them
                ;

// ---------------------
// HTML comment open.   HTML/XML comments may be placed around style sheets so that they
//                      are hidden from higher scope parsing engines such as HTML parsers.
//                      They comment open is therfore ignored by the CSS parser and we hide
//                      it from the ANLTR parser.
//
CDO             : '<!--' { $channel = 3; }  // CDO on channel 3 in case we want it later
                ;
    
// ---------------------            
// HTML comment close.  HTML/XML comments may be placed around style sheets so that they
//                      are hidden from higher scope parsing engines such as HTML parsers.
//                      They comment close is therfore ignored by the CSS parser and we hide
//                      it from the ANLTR parser.
//
CDC             : '-->' { $channel = 4; }  // CDC on channel 4 in case we want it later
                ;
                
INCLUDES        : '~=' ;
DASHMATCH       : '|=' ;

GREATER         : '>'  ;
LBRACE          : '{'  ;
RBRACE          : '}'  ;
LBRACKET        : '['  ;
RBRACKET        : ']'  ;
OPEQ            : '='  ;
SEMI            : ';'  ;
COLON           : ':'  ;
SOLIDUS         : '/'  ;
MINUS           : '-'  ;
PLUS            : '+'  ;
STAR            : '*'  ;
LPAREN          : '('  ;
RPAREN          : ')'  ;
COMMA           : ','  ;
DOT             : '.'  ;
PERCENT         : '%'  ;

// -----------------
// Literal strings. Delimited by either ' or "
//
fragment    INVALID :;
STRING          : '\'' ( ~('\n'|'\r'|'\f'|'\'') )* ( '\'' | { $type = INVALID; } )
                | '"'  ( ~('\n'|'\r'|'\f'|'"')  )* ( '"'  | { $type = INVALID; } )
                ;

// Some special identifiers
EXPRESSION : E X P R E S S I O N        ;
ALPHA      : A L P H A                  ;
CHARSET    : 'charset'                  ;
IMPORT_SYM : I M P O R T                ;
PAGE_SYM   : P A G E                    ;
MEDIA_SYM  : M E D I A                  ;


// -------------
// Identifier.  Identifier tokens pick up properties names and values
//
IDENT           : '-'? NMSTART NMCHAR*  ;

// -------------
// Reference.   Reference to an element in the body we are styling, such as <XXXX id="reference">
//
HASH            : '#' NAME              ;


IMPORTANT_SYM   : '!' (WS|COMMENT)* I M P O R T A N T   ;

// ---------
// Numbers. Numbers can be followed by pre-known units or unknown units
//          as well as '%' it is a precentage. Whitespace cannot be between
//          the numebr and teh unit or percent. Hence we scan any numeric, then
//          if we detect one of the lexical sequences for unit tokens, we change
//          the lexical type dynamically.
//
//          Here we first define the various tokens, then we implement the
//          number parsing rule.
//
/*
fragment    EMS         :;  // 'em'
fragment    EXS         :;  // 'ex'
fragment    LENGTH      :;  // 'px'. 'cm', 'mm', 'in'. 'pt', 'pc'
fragment    ANGLE       :;  // 'deg', 'rad', 'grad'
fragment    TIME        :;  // 'ms', 's'
fragment    FREQ        :;  // 'khz', 'hz'
fragment    DIMENSION   :;  // nnn'Somethingnotyetinvented'
fragment    PERCENTAGE  :;  // '%'
*/

fragment UNIT
    : IDENT
    | PERCENT
    ;

NUMBER
    :   ( (MINUS WS*)? (DIGIT+ (DOT DIGIT*)? | DOT DIGIT+) ) UNIT? 
    ;
    
// ------------
// url and uri.
//
URI :   U R L 
         (
             '(' WS* (STRING) WS* ')'
           | '(' WS*  URL_NO_WS (~('\n'|'\r'|'\f'|'\''|'"'|')'))+ URL_NO_WS WS* ( ')' | { $type = INVALID; } )
         )
    ;

fragment URL_NO_WS  : ~('\n'|'\r'|'\f'|'\''|'"'|')'|' '|'\t')  ;

// -------------
// Whitespace.  Though the W3 standard shows a Yacc/Lex style parser and lexer
//              that process the whitespace within the parser, ANTLR does not
//              need to deal with the whitespace directly in the parser.
//
WS : ( ' ' | '\t' | '\r' | '\n' | '\f' )+ ;

// -------------
//  Illegal.    Any other character shoudl not be allowed.
//
