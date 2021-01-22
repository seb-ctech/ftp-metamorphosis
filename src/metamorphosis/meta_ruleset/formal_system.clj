(ns metamorphosis.meta-ruleset.formal-system)

;=== PROTOTYPE OF AN ABSTRACT DATA STRUCTURE ===

; One generation contains the generation count for the scope a sequence of objects that define a class,
; that has a different meaning by the translation context it is used in and an index for the list of 
; commands of the same class. Classes can be of type: body, transform, property, amount.

; UNIT: Represents a unit of the final composition that is perceptible: [GraphX] Draws some form of geometry; [Mutation] builds some sequence
; TRANSFORM: Changes the relation between units in some form
; PROPERTY: Some property that the next units will have
; AMOUNT: Some amount that either the next Unit, Transform or Property will have. Default is "0"

(def example-theorem {:gen 0 
                      :sequence [{:class :transform :index 1}
                                 {:class :unit :index 2}
                                 {:class :amount :index 1}
                                 {:class :transform :index 1}
                                 {:class :property :index 2}
                                 {:class :property :index 0}
                                 {:class :amount :index 2}]})


; The alphabet is made out of the different classes that commands can have

(def alphabet [:unit :transform :property :amount])

(defn random-entry []
    {:class (get alphabet 
                 (rand-int (count alphabet)))
     :index (rand-int 5)})

(defn build-axiom [sequence]
    {:gen 0 :sequence sequence})

(defn build-random-axiom 
    ([]
        (build-random-axiom (+ (rand-int 6) 3)))
    ([length]
        (loop [sequence []]
            (if (< (count sequence) length)
                (recur (conj sequence 
                            (random-entry)))
                 (build-axiom sequence)))))

;TODO: Implement function to create axiom from input-data-structure provided by the "event-listener" module



