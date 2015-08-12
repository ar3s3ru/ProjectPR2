(** 
  * File:   projectSept.ml
  * Author: Danilo Cianfrone, 501292
  * Il programma è opera originale dell'autore, in ogni sua parte.
  *)

(* Definizione dell'ambiente *)
type 't env = (string * 't) list

(* Definizione del tipo tupla *)
type tuple =
    | Empty
    | ElemT of int * tuple

(* Definizione costrutti primitivi *)
type prim =
    | Int    of int
    | Bool   of bool
    | Funval of expr * prim env
    | Tuple  of tuple
    | Unbound
(* Definizione delle espressioni *)
and expr = 
    | Ide of string               (* Lookup dell'identificatore nell'ambiente *)
    | Val of prim                 (*              Valore costante             *)
    | And of expr * expr          (********************************************)
    | Or  of expr * expr          (*            Operazioni booleane           *)
    | Not of expr                 (********************************************)
    | OP  of string * expr * expr (*           Operazioni su interi           *)
    | Ifthenelse of expr * expr * expr   (* Condizionale           *)
    | Let   of string * expr * expr      (* Binding                *)
    | Fun   of string * expr             (* Registrazione Funzione *)
    | Apply of expr * expr               (* Valutazione funzione   *)

(* Funzione di lookup per il tipo *)
let rec lookup (ide : string) (env : prim env) =
    match env with
    | (id, value) :: env1 -> if ide = id then value else lookup ide env1
    | []                  -> Unbound

(* Interprete delle espressioni *)
let rec sem (exp : expr) (env : prim env) =
    match exp with
    (* Lookup dell'identificatore nell'ambiente *)
    | Ide id    -> lookup id env
    (* Valore costante, ritorna *)
    | Val value -> value
    (* And logico, valutato solo se e1 ed e2 sono booleani *)
    | And (e1, e2)      ->  let val1 = sem e1 env in
                                let val2 = sem e2 env in
                                    (match (val1, val2) with
                                    | (Bool b1, Bool b2) -> Bool (b1 && b2)
                                    | _                  -> failwith ("Wrong type"))
    (* Or logico, valutato solo se e1 ed e2 sono booleani *)
    | Or (e1, e2)       ->  let val1 = sem e1 env in
                                let val2 = sem e2 env in
                                    (match (val1, val2) with
                                    | (Bool b1, Bool b2) -> Bool (b1 || b2)
                                    | _                  -> failwith ("Wrong type"))
    (* Not logico, valutato solo se e1 è booleano *)
    | Not e1            ->  let val1 = sem e1 env in
                                (match val1 with
                                | Bool b -> Bool (not b)
                                | _      -> failwith ("Wrong type"))
    (* Operazioni su interi *)
    (* Moltiplicazione *)
    | OP ("*", e1, e2)  ->  let val1 = sem e1 env in
                                let val2 = sem e2 env in
                                    (match (val1, val2) with
                                    | (Int i1, Int i2) -> Int (i1 * i2)
                                    | _                -> failwith ("Wrong type"))
    (* Addizione *)
    | OP ("+", e1, e2)  ->  let val1 = sem e1 env in
                                let val2 = sem e2 env in
                                    (match (val1, val2) with
                                    | (Int i1, Int i2) -> Int (i1 + i2)
                                    | _                -> failwith ("Wrong type"))
    (* Sottrazione  *)
    | OP ("-", e1, e2)  ->  let val1 = sem e1 env in
                                let val2 = sem e2 env in
                                    (match (val1, val2) with
                                    | (Int i1, Int i2) -> Int (i1 - i2)
                                    | _                -> failwith ("Wrong type"))
    (* Uguaglianza *)
    | OP ("=", e1, e2)  ->  let val1 = sem e1 env in
                                let val2 = sem e2 env in
                                    (match (val1, val2) with
                                    | (Int i1, Int i2) -> Bool (i1 = i2)
                                    | _                -> failwith ("Wrong type"))
    (* Relazione minore o uguale *)
    | OP ("<=", e1, e2) ->  let val1 = sem e1 env in
                                let val2 = sem e2 env in
                                    (match (val1, val2) with
                                    | (Int i1, Int i2) -> Bool (i1 <= i2)
                                    | _                -> failwith ("Wrong type"))
    (* Non sono permesse operazioni d'altro tipo *)
    | OP (_, e1, e2)    -> failwith ("Invalid operation")
    (* Se la guarda g è booleano e uguale a true, esegui e1, altrimenti esegui e2, oppure *)
    (* fallisce la chiamata in caso di valore di g non booleano                           *)
    | Ifthenelse(g, e1, e2) ->  let gval = sem g env in
                                    (match gval with
                                    | Bool b -> if b then sem e1 env else sem e2 env
                                    | _      -> failwith ("Wrong type"))
    (* Estensione dell'ambiente corrente con il nuovo identificatore ide *)
    | Let (ide, e1, e2)     ->  let idval = sem e1 env in
                                    let new_env = (ide, idval) :: env in
                                        sem e2 new_env
    (* Definizione di una nuova funzione, scoping statico *)
    (* (assume env al momento della definizione)          *)
    | Fun (x, a)        -> Funval(exp, env)
    (* Applicazione funzionale *)
    | Apply (e1, e2)    ->  (match (sem e1 env) with
                            | Funval(Fun (x, exp), env1) -> sem exp ((x, (sem e2 env)) :: env1)
                            | _                          -> failwith ("No function in apply"))




