(ns aoc24.day17
  (:require
   [clojure.string :as s]
   [clojure.math :as m]
   [core :as c]))

(defn operand [operand regs]
  (case operand
    0 0
    1 1
    2 2
    3 3
    4 (:rega regs)
    5 (:regb regs)
    6 (:regc regs)
    7 :invalid))

(defn apply-instructions [instruction-pointer regs instructions out]
  (if (>= (inc instruction-pointer) (count instructions))
    {:regs regs :out out}
    (let [opcode (get instructions instruction-pointer)
          literal-operand (get instructions (inc instruction-pointer))
          combo (operand literal-operand regs)
          ;;
          ]
      (case opcode
        0 (recur (+ 2 instruction-pointer)
                 (assoc regs :rega (m/round (quot (:rega regs) (m/pow 2 combo))))
                 instructions
                 out)
        1 (recur (+ 2 instruction-pointer)
                 (assoc regs :regb (bit-xor (:regb regs) literal-operand))
                 instructions
                 out)
        2 (recur (+ 2 instruction-pointer)
                 (assoc regs :regb (mod combo 8))
                 instructions
                 out)
        3 (if (zero? (:rega regs))
            (recur (+ 2 instruction-pointer)
                   regs
                   instructions
                   out)
            (recur literal-operand
                   regs
                   instructions
                   out))
        4 (recur (+ 2 instruction-pointer)
                 (assoc regs :regb (bit-xor (:regb regs) (:regc regs)))
                 instructions
                 out)
        5 (recur (+ 2 instruction-pointer)
                 regs
                 instructions
                 (conj out (mod combo 8)))
        6 (recur (+ 2 instruction-pointer)
                 (assoc regs :regb (m/round (quot (:rega regs) (m/pow 2 combo))))
                 instructions
                 out)
        7 (recur (+ 2 instruction-pointer)
                 (assoc regs :regc (m/round (quot (:rega regs) (m/pow 2 combo))))
                 instructions
                 out)))))

(loop [i 11111111111111]
  (let [expected [2,4,1,5,7,5,1,6,0,3,4,2,5,5,3,0]
        actual (:out (apply-instructions 0 {:rega 11111111111111 :regb 0 :regc 0} [2,4,1,5,7,5,1,6,0,3,4,2,5,5,3,0] []))]
    (c/insp i)
    (if (= expected actual)
      i
      (if (> i 99999999999999)
        :error
        (recur (inc i))))))

(comment
  (= 1 (:regb (:regs (apply-instructions 0 {:regc 9} [2 6] []))))

  (= [0 1 2] (:out (apply-instructions 0 {:rega 10} [5,0 5,1 5,4] [])))

  (let [{:keys [regs out]} (apply-instructions 0 {:rega 2024} [0,1,5,4,3,0] [])]
    [(= 0 (:rega regs))
     (= [4,2,5,6,7,7,7,7,3,1,0] out)])

  (= 26 (:regb (:regs (apply-instructions 0 {:regb 29} [1,7] []))))

  (= 44354 (:regb (:regs (apply-instructions 0 {:regb 2024 :regc 43690} [4,0] []))))

  (let [text (slurp "./assets/day17/example.txt")
        [registers instructions] (s/split text #"\n\n")
        [rega regb regc] (c/extract-numbers registers)
        instructions (vec (c/extract-numbers instructions))
      ;;
        ]
    (s/join "," (:out (apply-instructions 0 {:rega rega :regb regb :regc regc} instructions []))))
  ;;

  (let [text (slurp "./assets/day17/example.txt")
        [registers instructions] (s/split text #"\n\n")
        [_ regb regc] (c/extract-numbers registers)
        instructions (vec (c/extract-numbers instructions))
      ;;
        ]
    [instructions  (:out (apply-instructions 0 {:rega 117440 :regb 0 :regc regc} instructions []))])
    ;;
  )

;;Register A: 44348299
;;Register B: 0
;;Register C: 0

;;Program: 2,4,1,5,7,5,1,6,0,3,4,2,5,5,3,0
