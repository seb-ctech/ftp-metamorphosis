(ns metamorphosis.event-listener.input)

;TODO: Process device-specific input to abstract data-structure

;;=== PROTOTYPE OF ABSTRACT INPUT DATA-STRUCTURE ===

; What about forms at the same time?
; Inputs are made out of signals, that have an intensity and a duration.
; A Signal represent a discrete input form that can be distinguished from others


(def test-input [
    {:signal :A :intensity 0.1 :duration 1/10}
    {:signal :B :intensity 0.1 :duration 3/10}
    {:signal :break :duration 2/10}
    {:signal :B :intensity 0.1 :duration 3/10}
])

(def input-interval 2000)
(def a-input "a")

(defn count-consecutive-letters [letters]
    (let [target (first letters)]
        (loop [n 1 
            remaining (rest letters)]
            (if (> (count remaining) 0)
                (if (= target (first remaining))
                    (recur (inc n) (rest remaining))
                    n)
                n))))

(defn string->input [string]
    (loop [letters string
           sequence [] ]
        (if (> (count letters) 0)
            (let [letter (str (first letters))
                  n (count-consecutive-letters letters)
                  signal (if (= letter " ") 
                             :break 
                             (keyword letter))
                  entry {:signal signal :duration (/ n 10)}]
                (recur (drop n letters)
                       (conj sequence (if (= (:signal entry) :break) 
                                          entry 
                                          (assoc entry :intensity 0.5)))))
            sequence)))

(defn build-input 
    [input-queue timer]
         (if (clojure.core/realized? timer)
             input-queue
             (do 
                 (Thread/sleep input-interval)
                 (build-input (conj input-queue a-input) timer))))
 
(defn record-input 
    ([time-frame]
        (record-input time-frame build-input))
    ([time-frame input]
    (input [] (clojure.core/future (Thread/sleep time-frame)))))