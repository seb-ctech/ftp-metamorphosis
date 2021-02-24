(ns metamorphosis.event-listener.input.command-line)

(defn count-consecutive-letters 
    "A function that returns the amount of letters that repeat continously"
    [letters]
    (let [target (first letters)]
        (loop [n 1 
            remaining (rest letters)]
            (if (> (count remaining) 0)
                (if (= target (first remaining))
                    (recur (inc n) (rest remaining))
                    n)
                n))))

(defn string->input 
    "A function that takes a command line string argument and converts it to an abstract input sequence"
    [string]
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
                                          (assoc entry :intensity (with-precision 2 (rand)))))))
            sequence)))

