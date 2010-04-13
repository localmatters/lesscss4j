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
parser grammar Css21Parser;

options {
    language=Java;
    tokenVocab = Css21Lexer;
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
    :   charSet
        imports*
        bodylist
     EOF
    ;
    
// -----------------
// Character set.   Picks up the user specified character set, should it be present.
//
charSet
    :   CHARSET_SYM STRING SEMI
    |
    ;

// ---------
// Import.  Location of an external style sheet to include in the ruleset.
//
imports
    :   IMPORT_SYM (STRING|URI) (medium (COMMA medium)*)? SEMI
    ;

// ---------
// Media.   Introduce a set of rules that are to be used if the consumer indicates
//          it belongs to the signified medium.
//
media
    : MEDIA_SYM medium (COMMA medium)*
        LBRACE
            ruleSet
        RBRACE
    ;

// ---------    
// Medium.  The name of a medim that are particulare set of rules applies to.
//
medium
    : IDENT 
    ;
    

bodylist
    : bodyset*
    ;
    
bodyset
    : ruleSet
    | media
    | page
    ;   
    
page
    : PAGE_SYM pseudoPage?
        LBRACE
            declaration SEMI (declaration SEMI)*
        RBRACE
    ;
    
pseudoPage
    : COLON IDENT
    ;
    
operator
    : SOLIDUS
    | COMMA
    |
    ;
    
combinator
    : PLUS
    | GREATER
    |
    ;
    
unaryOperator
    : MINUS
    | PLUS
    ;  
    
property
    : IDENT
    ;
    
ruleSet
    : selector (COMMA selector)*
        LBRACE
            declaration SEMI (declaration SEMI)*
        RBRACE
    ;
    
selector
    : simpleSelector (combinator simpleSelector)*
    ;

simpleSelector
    : elementName 
        ((esPred)=>elementSubsequent)*
        
    | ((esPred)=>elementSubsequent)+
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
    : DOT IDENT
    ;
    
elementName
    : IDENT
    | STAR
    ;
    
attrib
    : LBRACKET
    
        IDENT
        
            (
                (
                      OPEQ
                    | INCLUDES
                    | DASHMATCH
                )
                (
                      IDENT
                    | STRING
                )       
            )?
    
      RBRACKET
;

pseudo
    : COLON 
            IDENT
                ( // Function
                
                    LPAREN IDENT? RPAREN
                )?
    ;

declaration
    : property COLON expr prio?
    ;
    
prio
    : IMPORTANT_SYM
    ;
    
expr
    : term (operator term)*
    ;
    
term
    : unaryOperator?
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
    | IDENT (   // Function
                LPAREN expr RPAREN
            )?
    | URI
    | hexColor
    ;
    
hexColor
    : HASH
    ;
    
