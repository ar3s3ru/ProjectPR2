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

(*******************************************************)
(*******************************************************)
(**                 TEST INTERPRETE                   **)
(*******************************************************)
(*******************************************************)

(* Funzione di evalutazione e comparazione dei test *)
let run_test (test_exp: expr) (test_res: prim) (i: int) =
    let eval_test = sem test_exp [] in
        match test_res, eval_test with 
        | (Int k, Int h)     -> if k == h then (Printf.printf "Test %d superato!\n" i) else (Printf.printf "Test %d fallito...\n" i)
        | (Bool z, Bool v)   -> if z == v then (Printf.printf "Test %d superato!\n" i) else (Printf.printf "Test %d fallito...\n" i)
        | (Unbound, Unbound) -> (Printf.printf "Test %i superato!\n" i)
        | _ -> (Printf.printf "Test %d fallito, pattern invalido.\n" i)

(* Funzione di evalutazione delle espressioni scorrette; se torna Unbound
   allora failwith è stata catturata *)
let myFailwith (text_exp: expr) =
    try sem text_exp [] with
        Failure e -> Unbound

let check (text_exp: expr) (i: int) =
    let value = myFailwith text_exp in
        if value == Unbound then (Printf.printf "failwith in test %d catturata, ok!\n" i)
                            else (Printf.printf "failwith in test %d non catturata, errore...\n" i)

(* x non definita nell'ambiente, ritorna Unbound *)
let test1 = Ide("x")
let res_test1 = Unbound
(* x definito nell'ambiente, ritorna Int(12) *)
let test2 = Let("x", Val(Int(12)), Ide("x"))
let res_test2 = Int(12)
(* x definito, ritorna Bool(true) *)
let test3 = Let("x", Val(Bool(true)), Ide("x"))
let res_test3 = Bool(true)
(* Prova di And(true, false), ritorna Bool(false) *)
let test4 = Let("x", Val(Bool(true)),
                Let("y", Val(Bool(false)),
                    And(Ide("x"), Ide("y"))))
let res_test4 = Bool(false)
(* Prova di Or(false, true), ritorna Bool(true) *)
let test5 = Let("x", Val(Bool(false)),
                Let("y", Val(Bool(true)),
                    Or(Ide("x"), Ide("y"))))
let res_test5 = Bool(true)
(* Prova di Not(false) e Not(true), ritorna rispettivamente *)
(*                Bool(true) e Bool(false)                  *)
let test6 = Let("x", Val(Bool(false)), Not(Ide("x")))
let res_test6 = Bool(true)

let test7 = Let("x", Val(Bool(true)), Not(Ide("x")))
let res_test7 = Bool(false)
(* Prova And, Or e Not su tipi sbagliati *)
let test8 = Let("x", Val(Int(1)),
                Let("y", Val(Int(2)),
                    And(Ide("x"), Ide("y"))))
let test9 = Let("x", Val(Int(1)),
                Let("y", Val(Int(2)),
                    Or(Ide("x"), Ide("y"))))
let test10 = Let("x", Val(Int(1)), Not(Ide("x")))
(* Condizionale, ritorna Int(10) *)
let test11 = Ifthenelse(Val(Bool(true)), Val(Int(10)), Val(Int(11)))
let res_test11 = Int(10)
(* Condizionale, ritorna Bool(true) *)
let test12 = Let("x", Val(Bool(false)),
                 Ifthenelse(Ide("x"), Ide("x"), Not(Ide("x"))))
let res_test12 = Bool(true)
(* Condizionale, valore errato *)
let test13 = Let("x", Val(Int(19)),
                 Ifthenelse(Ide("x"), Ide("x"), Val(Int(1))))
(* Applicazione di funzione         *)
(* let x = 1 in                     *)
(*     let incr = fun x -> x + 1 in *)
(*         incr x ;;                *)
let test14 = Let ("x", Val (Int 1), 
                 Let ("incr", Fun ("x", OP("+", Ide "x", Val (Int 1))),
                     Apply(Ide "incr", Ide "x")))
let res_test14 = Int 2
(* let incr = fun x -> x + 1 in *)
(*     let x = 5 in             *)
(*         incr x ;;            *)
let test15 = Let ("incr",
                 Fun ("x", OP ("+", Ide "x", Val (Int 1))), 
                     Let ("x", Val (Int 5), 
                         Apply (Ide "incr", Ide "x")))
let res_test15 = Int 6

(* Alcune tuple definite per eseguire i test *)
let tuplexp1 = [Val(Int 1); Val(Int 2); Val(Int 3)]
let tuplexp2 = [Val(Int 4); Val(Int 7); Val(Int (-10))]
let tuplexp3 = [Val(Int 19); Val(Bool true); Val(Bool false); Val(Int 7)]

(* Test funzione At *)
let expr_test_1 = At(Val(Int 0), ExprTuple tuplexp1)
let res__test_1 = Int 1

let expr_test_2 = At(Val(Int 1), ExprTuple tuplexp2)
let res__test_2 = Int 7

let expr_test_3 = At(Val(Int 1000), ExprTuple tuplexp1) (* Eccezione in questo caso *)

(* Test funzione Fst *)
let expr_test_4 = Fst(Val(Int 3), ExprTuple tuplexp2)
let res__test_4 = Tuple (TElem (Int 4, TElem (Int 7, Elem (Int (-10)))))

let expr_test_5 = Fst(Val(Int 10), ExprTuple tuplexp1)  (* Eccezione, numero di el troppo alto *)

let expr_test_6 = Fst(Val(Int 2), ExprTuple tuplexp2)
let res__test_6 = Tuple (TElem (Int 4, Elem (Int 7)))

(* Test funzione Equals *)
let expr_test_7   = Equals(ExprTuple tuplexp1, ExprTuple tuplexp2)
let res__test_7   = Bool false

let expr_test_7_1 = Equals(ExprTuple tuplexp1, ExprTuple tuplexp1)
let res__test_7_1 = Bool true

(* Test funzione TFunApply *)
(* let incr = function x -> x + 10 *)
(*     in incr@[4; 7; -10]         *)
let expr_test_8  = Let ("incr", Fun ("x", OP("+", Ide "x", Val (Int 10))),
                     TFunApply(Ide "incr", ExprTuple tuplexp2))
let res__test_8  = Tuple (TElem (Int 14, TElem (Int 17, Elem (Int 0))))

(* let incr = function x -> x + 10         *)
(*     in incr@[19; true; false; 7]        *)
(*                                         *)
(* (Applicazione a tipi errati, eccezione) *)
let expr_test_9  = Let ("incr", Fun ("x", OP("+", Ide "x", Val (Int 10))),
                     TFunApply(Ide "incr", ExprTuple tuplexp3))

(* let incr = function x -> x + 10          *)
(*     in incr@([19; true; false; 7] fst 1) *)
let expr_test_10 = Let ("incr", Fun ("x", OP("+", Ide "x", Val (Int 10))),
                     TFunApply(Ide "incr", Fst(Val(Int 1), ExprTuple tuplexp3)))
let res__test_10 = Tuple (Elem (Int 29))

(* Espressione nella specifica del progetto *)
let expr_specific = Let ("add5", Fun ("x", OP("+", Ide "x", Val (Int 5))),
                        Let ("t", ExprTuple ([Val (Int 5); Val (Int 6); Val (Bool true); Val (Int 7)]), 
                            TFunApply(Ide "add5", Fst (Val (Int 2), Ide "t"))))
let res__specific = Tuple (TElem (Int 10, Elem (Int 11)))

(* Funzione entry point *)
let main () =
    (* Esegui i test *)
    run_test test1 res_test1 1 ;;
    run_test test2 res_test2 2 ;;
    run_test test3 res_test3 3 ;;
    run_test test4 res_test4 4 ;;
    run_test test5 res_test5 5 ;;
    run_test test6 res_test6 6 ;;
    run_test test7 res_test7 7 ;;
    check test8 8 ;;
    check test9 9 ;;
    check test10 10 ;;
    run_test test11 res_test11 11 ;;
    run_test test12 res_test12 12 ;;
    check test13 13 ;;
    run_test test14 res_test14 14 ;;
    run_test test15 res_test15 15 ;;

(* Entry point *)
main();;
