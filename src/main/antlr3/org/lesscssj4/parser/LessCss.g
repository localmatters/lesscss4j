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
    STYLESHEET;
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
    MIXIN_REF;
    MIXIN_MACRO;
    MIXIN_ARG;
    MIXIN_ACCESSOR;
    MEDIA_EXPR;
}

//@@JAVA@@

// -------------
// Main rule.   This is the main entry rule for the parser, the top level
//              grammar rule.
//
// A style sheet consists of an optional character set specification, an optional series
// of imports, and then the main body of style rules.
//
styleSheet
    : WS*
      (charSet WS*)?
      (importFile WS*)*
      (bodyset WS*)*
      EOF
      -> ^(STYLESHEET charSet* importFile* bodyset*)
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
    
importLocation 
    : STRING
    | URI ;

// ---------
// Media.   Introduce a set of rules that are to be used if the consumer indicates
//          it belongs to the signified medium.
//
media
    : '@' MEDIA_SYM WS+ medium (WS* COMMA WS* medium)* WS*
        LBRACE WS*
            (ruleList WS*)*
        RBRACE
    -> ^(MEDIA_SYM ^(MEDIA_EXPR medium)+ ruleList*)
    ;

// ---------    
// Medium.  The name of a medim that are particulare set of rules applies to.
//
medium
    : ((ONLY | NOT) WS+)? mediaType (WS+ AND WS+ mediaExpression)*
    | mediaExpression (WS+ AND WS+ mediaExpression)*
    ;
    
mediaType
    : ident 
    ;
    
mediaExpression
    : LPAREN WS* mediaFeature (WS* COLON WS* (ident | numberOrColor))? WS* RPAREN
    ;
    
mediaFeature
    : ident
    ;
    
bodyset
    : ruleList
    | media
    | page
    ;   
    
ruleList
    : variableDef
    | mixinMacro
    | ruleSet
    ;
    
page
    : '@' PAGE_SYM (WS+ COLON pseudoPage)? WS* LBRACE WS* (pageElement WS*)* RBRACE
    -> ^(PAGE_SYM pseudoPage? pageElement*)
    ;

pseudoPage
    : ident
    ;
    
pageElement
    : mixinSelectorList WS!* SEMI!
    | declaration
    | variableDef
    ;

combinator
    : WS* combinatorNonWs WS*
    | WS+
    ;
    
combinatorNonWs
    : PLUS
    | GREATER
    ;
    
mixinMacro
    : mixinMacroSelector WS* LPAREN WS* mixinMacroArg (WS* COMMA WS* mixinMacroArg)* WS* RPAREN WS* LBRACE (WS* ruleSetElement)+ WS* RBRACE
    -> ^(MIXIN_MACRO ^(SELECTOR mixinMacroSelector) mixinMacroArg+ ruleSetElement+)
    ;
    
mixinMacroArg
    : variable WS* COLON WS* mixinMacroArgDefault
    -> ^(MIXIN_ARG variable ^(EXPR mixinMacroArgDefault))
    ;
    
mixinMacroArgDefault
    : numberOrColor -> ^(CONSTANT numberOrColor)
    | literal       -> ^(LITERAL literal)
    ;
    
mixinMacroSelector
    : cssClass
    | HASH
    ;
            
ruleSet
    : ruleSetSelector WS* LBRACE (WS* ruleSetElement)* WS* RBRACE
    -> ^(RULESET ruleSetSelector ruleSetElement*)
    ;
    
ruleSetElement
    : (innerSelectorList WS* LBRACE)=>innerSelectorList WS* LBRACE (WS* ruleSetElement)* WS* RBRACE    -> ^(RULESET innerSelectorList ruleSetElement*)
    | (mixinSelectorList WS* SEMI)=>mixinSelectorList WS!* SEMI!
    | declaration
    | variableDef
    ;
    
mixinSelectorList
    : mixinSelector (WS!* COMMA! WS!* mixinSelector)*
    ;
    
mixinSelector
    : mixinMacroSelector WS* LPAREN (WS* mixinMacroCallArgList)? WS* RPAREN
      -> ^(MIXIN_REF ^(SELECTOR mixinMacroSelector) mixinMacroCallArgList*)
    | mixinNoArgSelector
      -> ^(MIXIN_REF ^(SELECTOR mixinNoArgSelector))
    ;
    
mixinNoArgSelector
    : mixinSimpleSelector (combinator mixinSimpleSelector)* 
    ;

mixinSimpleSelector
    : elementName (mixinSubsequent)*
    | mixinSubsequent+
    ;
    
mixinMacroCallArgList
    : mixinMacroCallArg (WS!* COMMA! WS!* mixinMacroCallArg)*
    ;
    
mixinMacroCallArg
    : literal             -> ^(MIXIN_ARG ^(LITERAL literal))
    | additiveExpression  -> ^(MIXIN_ARG ^(EXPR additiveExpression))
    ;
    
mixinSubsequent
    : HASH
    | cssClass
    ;
    
innerSelectorList
     : innerSelector (WS* COMMA WS* innerSelector)* -> ^(SELECTOR innerSelector)+
     ;

innerSelector
    : (combinatorNonWs WS*)? selector;
    
ruleSetSelector
    : fontFaceSelector -> ^(SELECTOR fontFaceSelector)
    | selectorList
    ;
    
fontFaceSelector
    : '@' FONT_FACE
    ;
    
selectorList
    : selector (WS* COMMA WS* selector)* -> ^(SELECTOR selector)+    
    ;

selector
    : simpleSelector (combinator simpleSelector)*
    ;

simpleSelector
    : elementName (elementSubsequent)*
    | elementSubsequent+
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
    : LBRACKET WS* ident (WS* attribOp WS* (ident | STRING | number))? WS* RBRACKET
    ;
    
attribOp
    : OPEQ
    | INCLUDES
    | DASHMATCH
    | START_MATCH
    | END_MATCH
    | SUBSTR_MATCH
    ;

pseudo
    : COLON COLON? ident (LPAREN WS* pseudoArg WS* RPAREN)?
    ;
    
pseudoArg
    : number
    | (ident|number) WS* (PLUS | MINUS) WS* number
    | selector
    ;
    
variableDef
    : variableExpr WS* SEMI!
    ;

variableExpr
    : variable WS* COLON WS* propertyValue
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
    | numberOrColor -> ^(CONSTANT numberOrColor)
    ;
    
numberOrColor
    : number
    | hexColor
    | RGB_COLOR
    | HSL_COLOR
    ;
    
number
    :  MINUS? NUMBER
    ;
    
// the 'font' property needs some special handling because it has syntax that looks like
// division, but really isn't.  For example, the declaration:
//     font: 12px/16px Arial;
// means that the font size should be set to 12px and the line height set to 16px
declaration
    : (fontProperty)=>fontDeclaration
    | (property WS* COLON (WS* propertyValue (WS* important)?)? WS* SEMI
    -> ^(DECLARATION property ^(PROP_VALUE propertyValue)? important?))
    ;
    
propPrefix
    : STAR
    | UNDERSCORE
    ;
    
fontDeclaration
    : fontProperty WS* COLON WS* fontPropertyValue (WS* important)? WS* SEMI
    -> ^(DECLARATION fontProperty ^(PROP_VALUE fontPropertyValue) important?)
    ;
    
fontProperty
    : propPrefix? FONT
    -> ^(FONT propPrefix?)
    ;

fontPropertyValue
    : ( 
        ( (fontStyle WS*)* fontSize (WS!* SOLIDUS WS!* lineHeight)? WS* fontFamily (WS!* COMMA WS* fontFamily)* )
        | ident
      )
    ;
    
fontFamily
    : ident
    | STRING
    | variable
    ;
    
lineHeight
    : ident
    | numberOrColor
    ;
    
fontSize
    : ident
    | numberOrColor
    ;

fontStyle
    : ident
    | NUMBER
    ;

property
    : propPrefix? identNoFont
    -> ^(identNoFont propPrefix?)
    ;
    
// Added optional EOF tokens so that this rule can be called directly.  This
// is necessary to support IE8 Alpha expressions
propertyValue
    // The SOLIDUS in the first branch is necessary in order to parse declarations of the form:
    //     cursor: url(img/cursors/grab.cur), default/9;
    : propertyTermNoExpr ((propTermSep|WS* SOLIDUS WS*) propertyTerm)* EOF?
    | (mixinMacroSelector WS* LBRACKET)=>mixinAccessor EOF?
    | (primaryExpression propTermSep propertyTerm)=>propertyTermExpression (propTermSep propertyTerm)+ EOF?
    | additiveExpression EOF? -> ^(EXPR additiveExpression)
    ;
    
mixinAccessor
    : mixinMacroSelector WS* LBRACKET WS* mixinAccessorItem WS* RBRACKET
    -> ^(MIXIN_ACCESSOR ^(SELECTOR mixinMacroSelector) mixinAccessorItem)
    ;

mixinAccessorItem
    : STRING
    | variable -> ^(VAR variable)
    ;

propertyTermExpression
    : primaryExpression -> ^(EXPR primaryExpression)
    ;
	        
propTermSep
    : WS* COMMA WS*
    | WS+
    ;
    
propertyTermNoExpr
    : (functionPred)=>function
    | literal -> ^(LITERAL literal)
    ;
    
functionPred
    : ident WS* LPAREN
    ;
        
propertyTerm
    : propertyTermNoExpr
    | propertyTermExpression
    ;

// Internet Explorer specific functions
function
    : (ALPHA)=>ieAlpha
    | (EXPRESSION)=>ieExpression
    | (ident WS* LPAREN functionArgList? RPAREN -> ^(FUNCTION ident functionArgList))
    ;
    
functionArgList
    : functionArg ((WS!* COMMA WS!*|WS+) functionArg)*
    ;
    
functionArg
    : propertyTerm
    ;
    
functionArgItem
    : (functionPred)=>function
    | literal
    | additiveExpression
    ;

ieAlpha
    : ALPHA WS* LPAREN WS* ieAlphaTerm (WS* COMMA WS* ieAlphaTerm)* WS* RPAREN
    -> ^(FUNCTION ALPHA ieAlphaTerm*)
    ;
    
ieAlphaTerm
    : ident WS* ieAlphaTermOp WS* (literal | additiveExpression)
    -> ^(ieAlphaTermOp ident ^(LITERAL literal)? ^(EXPR additiveExpression)?)
    ;
    
ieAlphaTermOp
    : OPEQ
    | COLON
    ;

ieExpression
    : EXPRESSION WS* LPAREN ieExprTerm RPAREN 
    -> ^(FUNCTION EXPRESSION ieExprTerm)
    ;
    
ieExprTerm
    : (STRING|'&'|'?'|~('&'|'?'|'('|')'|STRING))*
      ( LPAREN ieExprTerm RPAREN (STRING | ~('('|')'|STRING))* )*
    ;

important
    : IMPORTANT_SYM
    ;   

hexColor 
    : HASH
    ;

identNoFont    
    : IDENT 
    | ALPHA 
    | EXPRESSION
    | CHARSET
    | MEDIA_SYM
    | IMPORT_SYM
    | PAGE_SYM
    | AND
    | ONLY
    | NOT
    ;
    
ident
    : identNoFont
    | FONT
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
                                ;
//                                ('\r'|'\n'|'\t'|'\f'|' ')*  ;
                                
fragment ESCAPE
    : UNICODE
    | '\\' ~('\r'|'\n'|'\f'|HEXCHAR)
    ;

fragment NMSTART
    : '_'
    | 'a'..'z'
    | 'A'..'Z'
    | NONASCII
    | ESCAPE
    ;

fragment NMCHAR
    : NMSTART
    | DIGIT
    | MINUS
    ;

// Basic Alpha characters in upper, lower and escaped form. Note that
// whitespace and newlines are unimportant even within keywords. We do not
// however call a further fragment rule to consume these characters for
// reasons of performance - the rules are still eminently readable.
//
fragment A      : ('a'|'A') | '\\' ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')'1' ;
fragment B      : ('b'|'B') | '\\' ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')'2' ;
fragment C      : ('c'|'C') | '\\' ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')'3' ;
fragment D      : ('d'|'D') | '\\' ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')'4' ;
fragment E      : ('e'|'E') | '\\' ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')'5' ;
fragment F      : ('f'|'F') | '\\' ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')'6' ;
fragment G      : ('g'|'G') | '\\' ( 'g' | 'G' | ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')'7' ) ;
fragment H      : ('h'|'H') | '\\' ( 'h' | 'H' | ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')'8' ) ;
fragment I      : ('i'|'I') | '\\' ( 'i' | 'I' | ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')'9' ) ;
fragment J      : ('j'|'J') | '\\' ( 'j' | 'J' | ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')('A'|'a') ) ;
fragment K      : ('k'|'K') | '\\' ( 'k' | 'K' | ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')('B'|'b') ) ;
fragment L      : ('l'|'L') | '\\' ( 'l' | 'L' | ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')('C'|'c') ) ;
fragment M      : ('m'|'M') | '\\' ( 'm' | 'M' | ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')('D'|'d') ) ;
fragment N      : ('n'|'N') | '\\' ( 'n' | 'N' | ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')('E'|'e') ) ;
fragment O      : ('o'|'O') | '\\' ( 'o' | 'O' | ('0' ('0' ('0' '0'?)?)?)? ('4'|'6')('F'|'f') ) ;
fragment P      : ('p'|'P') | '\\' ( 'p' | 'P' | ('0' ('0' ('0' '0'?)?)?)? ('5'|'7')('0') ) ;
fragment Q      : ('q'|'Q') | '\\' ( 'q' | 'Q' | ('0' ('0' ('0' '0'?)?)?)? ('5'|'7')('1') ) ;
fragment R      : ('r'|'R') | '\\' ( 'r' | 'R' | ('0' ('0' ('0' '0'?)?)?)? ('5'|'7')('2') ) ;
fragment S      : ('s'|'S') | '\\' ( 's' | 'S' | ('0' ('0' ('0' '0'?)?)?)? ('5'|'7')('3') ) ;
fragment T      : ('t'|'T') | '\\' ( 't' | 'T' | ('0' ('0' ('0' '0'?)?)?)? ('5'|'7')('4') ) ;
fragment U      : ('u'|'U') | '\\' ( 'u' | 'U' | ('0' ('0' ('0' '0'?)?)?)? ('5'|'7')('5') ) ;
fragment V      : ('v'|'V') | '\\' ( 'v' | 'V' | ('0' ('0' ('0' '0'?)?)?)? ('5'|'7')('6') ) ;
fragment W      : ('w'|'W') | '\\' ( 'w' | 'W' | ('0' ('0' ('0' '0'?)?)?)? ('5'|'7')('7') ) ;
fragment X      : ('x'|'X') | '\\' ( 'x' | 'X' | ('0' ('0' ('0' '0'?)?)?)? ('5'|'7')('8') ) ;
fragment Y      : ('y'|'Y') | '\\' ( 'y' | 'Y' | ('0' ('0' ('0' '0'?)?)?)? ('5'|'7')('9') ) ;
fragment Z      : ('z'|'Z') | '\\' ( 'z' | 'Z' | ('0' ('0' ('0' '0'?)?)?)? ('5'|'7')('A'|'a') ) ;
fragment DIGIT  : '0'..'9'    ;
fragment SIGN   : PLUS | MINUS ;

// -------------
// Comments.    Comments may not be nested, may be multilined and are delimited
//              like C comments: /* ..... */
//              LessCSS supports line comments too of the form //....
//              COMMENTS are hidden from the parser which simplifies the parser 
//              grammar a lot.
//
COMMENT
    :   '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    |   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    ;

// ---------------------
// HTML comment open.   HTML/XML comments may be placed around style sheets so that they
//                      are hidden from higher scope parsing engines such as HTML parsers.
//                      They comment open is therfore ignored by the CSS parser and we hide
//                      it from the ANLTR parser.
//
CDO : '<!--' { $channel=HIDDEN; }  // CDO on channel 3 in case we want it later
    ;

// ---------------------            
// HTML comment close.  HTML/XML comments may be placed around style sheets so that they
//                      are hidden from higher scope parsing engines such as HTML parsers.
//                      They comment close is therfore ignored by the CSS parser and we hide
//                      it from the ANLTR parser.
//
CDC : '-->' { $channel=HIDDEN; }  // CDC on channel 4 in case we want it later
    ;
                
INCLUDES        : '~=' ;
DASHMATCH       : '|=' ;

// These are CSS3 constructs
START_MATCH     : '^=' ;
END_MATCH       : '$=' ;
SUBSTR_MATCH    : '*=' ;

// Individual characters used a lot
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
UNDERSCORE      : '_'  ;

// -----------------
// Literal strings. Delimited by either ' or "
//
fragment    INVALID :;

STRING     : '\'' ( ~('\n'|'\r'|'\f'|'\'') )* ( '\'' | { $type = INVALID; } )
           | '"'  ( ~('\n'|'\r'|'\f'|'"')  )* ( '"'  | { $type = INVALID; } )
           ;

// Some special identifiers
ALPHA      : A L P H A                  ;
EXPRESSION : E X P R E S S I O N        ;
CHARSET    : 'charset'                  ;
IMPORT_SYM : I M P O R T                ;
PAGE_SYM   : P A G E                    ;
MEDIA_SYM  : M E D I A                  ;
FONT_FACE  : F O N T '-' F A C E        ;
FONT       : F O N T                    ;
ONLY       : O N L Y                    ;
NOT        : N O T                      ;
AND        : A N D                      ;

RGB_COLOR
    : R G B   WS* LPAREN WS* NUMBER WS* COMMA WS* NUMBER WS* COMMA WS* NUMBER WS* RPAREN
    | R G B A WS* LPAREN WS* NUMBER WS* COMMA WS* NUMBER WS* COMMA WS* NUMBER WS* COMMA WS* NUMBER WS* RPAREN 
    ;
    
HSL_COLOR
    : H S L   WS* LPAREN WS* NUMBER WS* COMMA WS* NUMBER WS* COMMA WS* NUMBER WS* RPAREN
    | H S L A WS* LPAREN WS* NUMBER WS* COMMA WS* NUMBER WS* COMMA WS* NUMBER WS* COMMA WS* NUMBER WS* RPAREN 
    ;

// -------------
// Identifier.  Identifier tokens pick up properties names and values
//
IDENT      : MINUS? NMSTART NMCHAR* ;

// -------------
// Reference.   Reference to an element in the body we are styling, such as <XXXX id="reference">
//
HASH       : '#' NMCHAR+           ;


IMPORTANT_SYM   : '!' (WS)* I M P O R T A N T   ;

// ---------
// Numbers. Numbers can be followed by pre-known units or unknown units
//          as well as '%' it is a precentage. Whitespace cannot be between
//          the number and the unit or percent.
fragment UNIT
    : ('a'..'z' | 'A'..'Z' | NONASCII | ESCAPE)+ | PERCENT
    ;

NUMBER
    :   ( (DIGIT+ (DOT DIGIT*)? | DOT DIGIT+) ) UNIT?
    ;

// ------------
// url and uri.
//
URI :   U R L 
         (
             LPAREN WS* (STRING) WS* RPAREN
           | LPAREN WS*  URL_NO_WS (~('\n'|'\r'|'\f'|'\''|'"'|RPAREN)* URL_NO_WS)? WS* ( RPAREN | { $type = INVALID; } )
         )
    ;

fragment URL_NO_WS  : ~('\n'|'\r'|'\f'|'\''|'"'|RPAREN|' '|'\t')  ;

// -------------
// Whitespace.  Though the W3 standard shows a Yacc/Lex style parser and lexer
//              that process the whitespace within the parser, ANTLR does not
//              need to deal with the whitespace directly in the parser.
//
WS : ( ' ' | '\t' | '\r' | '\n' | '\f' )+ ;

// -------------
//  Illegal.    Any other character shoudl not be allowed.
//
