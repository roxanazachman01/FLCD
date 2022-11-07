classDiagram
direction BT
class Scanner {
  + Scanner(String, String, String, String) 
  - detect(String) List~String~
  - writeST() void
  - isSymbol(String) boolean
  - checkIfOperator(String, int) String?
  - codify(List~String~) void
  - classify(List~String~, int) String
  + printFileContent() void
  - writePIF() void
  - isValidConstant(String) boolean
  - isValidIdentifier(String) boolean
  - loadTokens() void
  - writeToFile() void
  + tokenize() void
  - isWordChar(char) boolean
}

