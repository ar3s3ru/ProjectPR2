(** 
  * File:   projectSept.ml
  * Author: Danilo Cianfrone, 501292
  * Il programma è opera originale dell'autore, in ogni sua parte.
  *)

(* Definizione dell'ambiente *)
type 't env = (string * 't) list

(* Definizione del tipo tupla *)
type tuple =
    | Elem  of prim
    | TElem of prim * tuple
(* Definizione costrutti primitivi *)
and prim =
    | Int    of int
    | Bool   of bool
    | Funval of expr * prim env
    | Tuple  of tuple
    | Unbound
(* Definizione delle espressioni *)
and expr = 
    | Ide        of string               (* Lookup dell'identificatore nell'ambiente *)
    | Val        of prim                 (*              Valore costante             *)
    | And        of expr * expr          (********************************************)
    | Or         of expr * expr          (*            Operazioni booleane           *)
    | Not        of expr                 (********************************************)
    | OP         of string * expr * expr (*           Operazioni su interi           *)
    | Ifthenelse of expr * expr * expr   (*              Condizionale                *)
    | Let        of string * expr * expr (*                 Binding                  *)
    | Fun        of string * expr        (*          Registrazione Funzione          *)
    | Apply      of expr * expr          (*           Valutazione funzione           *)
    (* Funzioni relative alle tuple **************************************************)
    | ExprTuple  of expr list
    | Equals     of expr * expr
    | At         of expr * expr
    | Fst        of expr * expr
    | TFunApply  of expr * expr

(* Check di equalità tra tuple *)
let rec checkTuples (t1 : tuple) (t2 : tuple) =
    match (t1, t2) with
    | (Elem i1, Elem i2)                 -> if i1 = i2 then true else false
    | (TElem (i1, nt1), TElem (i2, nt2)) -> if i1 = i2 then (true && checkTuples nt1 nt2)
                                                       else false
    | _                                  -> false

(* Funzione get dell'elemento all'indice index di una tupla *)
let rec getElem (index : int) (tupl : tuple) =
    match tupl with
    | Elem i       -> if index = 0 then i else failwith ("Index out of bound")
    | TElem (i, t) -> if index = 0 then i else getElem (index - 1) t

(* Seleziona i primi elems elementi della tupla tupl (contando fino a uno) *)
let rec selectFirst (elems : int) (tupl : tuple) =
    match tupl with
    | Elem i       -> if elems > 1 then failwith ("Tuple has not that much elements!")
                      else if elems = 1 then Elem i
                           else failwith ("Negative elems specified in base case")
    | TElem (i, t) -> if elems <= 0 then failwith ("Wrong counting elements specified")
                      else if elems = 1 then Elem i
                           else TElem(i, selectFirst (elems - 1) t)

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
    | Fun (x, a)        ->  Funval(exp, env)
    (* Applicazione funzionale *)
    | Apply (e1, e2)    ->  (match (sem e1 env) with
                            | Funval(Fun (x, exp), env1) -> sem exp ((x, (sem e2 env)) :: env1)
                            | _                          -> failwith ("No function in apply"))
    (* Espressione di una tupla *)
    | ExprTuple exp1    -> 
        (* Risolve una lista di espressioni dal quale costruire una tupla *)
        let rec tupleResolve (exp : expr list) = 
            (match exp with
            | []      -> failwith ("Cannot resolve empty expression list")
            | [x]     -> let xval = sem x env in
                            (match xval with
                            | Unbound -> failwith ("Unbound value")
                            | _       -> Elem xval)
            | x :: xs -> let xval = sem x env in
                            (match xval with
                            | Unbound -> failwith ("Unbound value")
                            | _       -> TElem (xval, tupleResolve xs))) 
            in Tuple (tupleResolve exp1)
    (* Confronto tra tuple *)
    | Equals (exp1, exp2) -> let val1 = sem exp1 env in
                                 let val2 = sem exp2 env in
                                    (match (val1, val2) with
                                    | Tuple t1, Tuple t2 -> Bool (checkTuples t1 t2)
                                    | _                  -> failwith ("Operator defined only between tuples"))
    (* Accesso elemento tupla *)
    | At (index, exp1)    -> let val_id = sem index env in
                                 let val_exp = sem exp1 env in
                                     (match (val_id, val_exp) with
                                     | (Int i, Tuple t) -> if i < 0 then failwith ("Wrong index specified")
                                                                    else getElem i t
                                     | _                -> failwith ("Wrong syntax"))
    (* Selezione primi elems elementi *)
    | Fst (elems, exp1)   -> let val_el = sem elems env in
                                 let val_exp = sem exp1 env in
                                     (match (val_el, val_exp) with
                                     | (Int i, Tuple t) -> Tuple (selectFirst i t)
                                     | _                -> failwith ("Wrong syntax"))
    (* Applicazione di funzione agli elementi di una tupla *)
    | TFunApply (exp1, exp2) -> (match sem exp2 env with
                                | Tuple t -> let val1 = sem exp1 env in
                                                let rec applyFun (func : prim) (tupl : tuple) =
                                                (match (func, tupl) with
                                                | (Funval(Fun (x, exp), env1), Elem i)        ->  Elem (sem exp ((x, i)::env1))
                                                | (Funval(Fun (x, exp), env1), TElem (i, t1)) -> TElem (sem exp ((x, i)::env1), applyFun func t1)
                                                | _ -> failwith ("No function passed as first argument"))
                                                    in Tuple (applyFun (sem exp1 env) t)
                                | _ -> failwith ("Wrong type"))

let tuplexp1 = [Val(Int 1); Val(Int 2); Val(Int 3)]
let tuplexp2 = [Val(Int 4); Val(Int 7); Val(Int (-10))]
let tuplexp3 = [Val(Int 19); Val(Bool true); Val(Bool false); Val(Int 7)]

let expr_test_1 = At(Val(Int 0), ExprTuple tuplexp1)
let expr_test_2 = At(Val(Int 1), ExprTuple tuplexp2)
let expr_test_3 = At(Val(Int 1000), ExprTuple tuplexp1)

let expr_test_4 = Fst(Val(Int 3), ExprTuple tuplexp2)
let expr_test_5 = Fst(Val(Int 10), ExprTuple tuplexp1)
let expr_test_6 = Fst(Val(Int 2), ExprTuple tuplexp2)

let expr_test_7 = Equals(ExprTuple tuplexp1, ExprTuple tuplexp2)
let expr_test_8 = Let ("incr", Fun ("x", OP("+", Ide "x", Val (Int 10))),
                    TFunApply(Ide "incr", ExprTuple tuplexp2))
let expr_test_8 = Let ("incr", Fun ("x", OP("+", Ide "x", Val (Int 10))),
                    TFunApply(Ide "incr", ExprTuple tuplexp3))
let expr_test_9 = Let ("incr", Fun ("x", OP("+", Ide "x", Val (Int 10))),
                    TFunApply(Ide "incr", Fst(Val(Int 1), ExprTuple tuplexp3)))

(* Espressione nella specifica del progetto *)
let expr_specific = Let ("add5", Fun ("x", OP("+", Ide "x", Val (Int 5))),
                        Let ("t", ExprTuple ([Val (Int 5); Val (Int 6); Val (Bool true); Val (Int 7)]), 
                            TFunApply(Ide "add5", Fst (Val (Int 2), Ide "t"))))

