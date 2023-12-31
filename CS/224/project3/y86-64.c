#include <stdio.h>
#include <stdlib.h>
#include "utils.h"

const int MAX_MEM_SIZE  = (1 << 13);

void fetchStage(int *icode, int *ifun, int *rA, int *rB, wordType *valC, wordType *valP) {
wordType PC_Address = getPC();
byteType PC_Instruction = getByteFromMemory(PC_Address);
*ifun = PC_Instruction & 0x0f;
*icode = PC_Instruction >> 4;

if ((*icode == NOP)||(*icode == HALT)||(*icode == RET)){
  *valP = PC_Address +1;
}
if ((*icode == RRMOVQ)||(*icode == OPQ)||(*icode == PUSHQ)||(*icode == POPQ)){
  *rB = getByteFromMemory(PC_Address+1) & 0x0f;
  *rA = getByteFromMemory(PC_Address+1) >> 4;
  *valP = PC_Address +2;
}
if ((*icode == IRMOVQ)||(*icode == RMMOVQ)||(*icode == MRMOVQ)){
  *rB = getByteFromMemory(PC_Address+1) & 0x0f;
  *rA = getByteFromMemory(PC_Address+1) >> 4;
  *valC = getWordFromMemory(PC_Address+2);
  *valP = PC_Address +10;
}
if ((*icode == JXX)||(*icode == CALL)){
  *valC = getWordFromMemory(PC_Address+1);
  *valP = PC_Address + 9;
}
}

void decodeStage(int icode, int rA, int rB, wordType *valA, wordType *valB) {
if ((icode == RRMOVQ)||(icode == PUSHQ)){
  *valA = getRegister(rA);
}
if ((icode == OPQ)||(icode == RMMOVQ)){
  *valA = getRegister(rA);
  *valB = getRegister(rB);
}
if (icode == MRMOVQ){
  *valB = getRegister(rB);
}
if ((icode == CALL)||(icode == PUSHQ)){
  *valB = getRegister(RSP);
}
if ((icode == RET)||(icode == POPQ)){
  *valA = getRegister(RSP);
  *valB = getRegister(RSP);
}
}

void executeStage(int icode, int ifun, wordType valA, wordType valB, wordType valC, wordType *valE, bool *Cnd) {
bool sf = FALSE;
bool zf = FALSE;
bool of = FALSE;
if (icode == RRMOVQ){
  *valE = 0+valA;
}
if (icode == IRMOVQ){
  *valE = 0+valC;
}
if (icode == OPQ){
  if (ifun == ADD){
    *valE = valB + valA;
    if (((valA < 0) == (valB <0))&&((*valE <0)!=(valA <0))){
	of = TRUE;
    }
  }
  if (ifun == SUB){
    *valE = valB - valA;
    if (((valA > 0) == (valB <0))&&((*valE <0)!=(valA >0))){
        of = TRUE;
    }
}
  if (ifun == AND){*valE = valB & valA;}
  if (ifun == XOR){*valE = valB ^ valA;}

  if (*valE == 0){zf = TRUE;}
  if (*valE <0) {sf = TRUE;}

  setFlags(sf,zf,of);
}
if ((icode == RMMOVQ)||(icode == MRMOVQ)){
  *valE = valB + valC;
}
if (icode == JXX){
  *Cnd = Cond(ifun);
}
if ((icode == CALL)||(icode == PUSHQ)){
  *valE = valB -8;
}
if ((icode == RET)||(icode == POPQ)){
  *valE = valB +8;
}
}

void memoryStage(int icode, wordType valA, wordType valP, wordType valE, wordType *valM) {
if ((icode == RMMOVQ)||(icode == PUSHQ)){setWordInMemory(valE, valA);}
if (icode == MRMOVQ){*valM = getWordFromMemory(valE);}
if (icode == CALL){setByteInMemory(valE, valP);}
if ((icode == RET)||(icode == POPQ)){*valM = getWordFromMemory(valA);}
}

void writebackStage(int icode, int rA, int rB, wordType valE, wordType valM) {
if ((icode == RRMOVQ)||(icode == IRMOVQ)||(icode == OPQ)){setRegister(rB, valE);}
if ((icode == MRMOVQ)||(icode == POPQ)){setRegister(rA, valM);}
if ((icode == CALL)||(icode == RET)||(icode == POPQ)||(icode == PUSHQ)){setRegister(RSP, valE);}

}

void pcUpdateStage(int icode, wordType valC, wordType valP, bool Cnd, wordType valM) {
if ((icode != JXX)||(icode != CALL)||(icode != RET)){setPC(valP);}
if (icode == JXX){setPC(Cnd?valC:valP);}
if (icode == CALL){setPC(valC);}
if (icode == RET){setPC(valM);}
if (icode == HALT){
  setStatus(STAT_HLT);
}
}

void stepMachine(int stepMode) {
  /* FETCH STAGE */
  int icode = 0, ifun = 0;
  int rA = 0, rB = 0;
  wordType valC = 0;
  wordType valP = 0;
 
  /* DECODE STAGE */
  wordType valA = 0;
  wordType valB = 0;

  /* EXECUTE STAGE */
  wordType valE = 0;
  bool Cnd = 0;

  /* MEMORY STAGE */
  wordType valM = 0;

  fetchStage(&icode, &ifun, &rA, &rB, &valC, &valP);
  applyStageStepMode(stepMode, "Fetch", icode, ifun, rA, rB, valC, valP, valA, valB, valE, Cnd, valM);

  decodeStage(icode, rA, rB, &valA, &valB);
  applyStageStepMode(stepMode, "Decode", icode, ifun, rA, rB, valC, valP, valA, valB, valE, Cnd, valM);
  
  executeStage(icode, ifun, valA, valB, valC, &valE, &Cnd);
  applyStageStepMode(stepMode, "Execute", icode, ifun, rA, rB, valC, valP, valA, valB, valE, Cnd, valM);
  
  memoryStage(icode, valA, valP, valE, &valM);
  applyStageStepMode(stepMode, "Memory", icode, ifun, rA, rB, valC, valP, valA, valB, valE, Cnd, valM);
  
  writebackStage(icode, rA, rB, valE, valM);
  applyStageStepMode(stepMode, "Writeback", icode, ifun, rA, rB, valC, valP, valA, valB, valE, Cnd, valM);
  
  pcUpdateStage(icode, valC, valP, Cnd, valM);
  applyStageStepMode(stepMode, "PC update", icode, ifun, rA, rB, valC, valP, valA, valB, valE, Cnd, valM);

  incrementCycleCounter();
}

/** 
 * main
 * */
int main(int argc, char **argv) {
  int stepMode = 0;
  FILE *input = parseCommandLine(argc, argv, &stepMode);

  initializeMemory(MAX_MEM_SIZE);
  initializeRegisters();
  loadMemory(input);

  applyStepMode(stepMode);
  while (getStatus() != STAT_HLT) {
    stepMachine(stepMode);
    applyStepMode(stepMode);
#ifdef DEBUG
    printMachineState();
    printf("\n");
#endif
  }
  printMachineState();
  return 0;
}
