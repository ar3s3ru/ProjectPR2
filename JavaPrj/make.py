#!/usr/bin/env python3

import os, sys

global filesToBuild = \
["DigitalDoc", "EmptyQueueException", "SharedDoc", \
 "ShareDoc", "ShareDocImpl", "User", "WrongIDException"]

def checkPosition(path = "."):
    # Fa un check sulla posizione attuale.
    listFiles = os.listdir(path)
    # Se c'è la cartella del progetto, ritorna true.
    for File in listFile:
        if File == "ShareDoc":
            return True
    # Se termina il ciclo, la cartella non è stata trovata.
    return False

# Esegue lo script solo da command line
if __name__ == "__main__" :
    # Esegui lo script di compilazione
    if not checkPosition():
        print("Errore: cartella ShareDoc non trovata.")
        print("Exiting...")
        sys.exit(1)

    for fileToBuild in filesToBuild:
        


    