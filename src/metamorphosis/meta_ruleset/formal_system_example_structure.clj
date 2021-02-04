(ns metamorphosis.meta-ruleset.formal-system-example-structure)
;Starting Motif
(def simple
    {:gen 0, :sequence [{:class :amount, :index 1} {:class :unit, :index 3} {:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :property, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :transform, :index 0} {:class :amount, :index 2} {:class :unit, :index 2}]})
 
;Resulting Movement
(def complex
{:gen 3, :sequence [
    {:class :transform, :index 3} 
    {:class :transform, :index 1} 
    {:gen 2, :sequence [
        {:class :property, :index 0} 
        {:class :property, :index 3} 
        {:gen 1, :sequence [
            {:class :transform, :index 3} 
            {:class :transform, :index 0} 
            {:gen 0, :sequence [{:class :unit, :index 3} {:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :amount, :index 4} {:class :transform, :index 0} {:class :unit, :index 2}]} 
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:gen 0, :sequence [{:class :unit, :index 3} {:class :transform, :index 0} {:class :amount, :index 3} {:class :amount, :index 4} {:class :amount, :index 4} {:class :transform, :index 0} {:class :unit, :index 2}]} 
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]}
        ]} 
        {:class :property, :index 2} 
        {:class :property, :index 2} 
        {:gen 1, :sequence [
            {:class :transform, :index 3} 
            {:class :transform, :index 0} 
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:class :property, :index 4} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]}
        ]} 
        {:class :property, :index 2} 
        {:class :property, :index 2} 
        {:gen 1, :sequence [
            {:class :transform, :index 3} 
            {:class :transform, :index 0} 
            {:gen 0, :sequence [{:class :unit, :index 3} {:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :amount, :index 4} {:class :transform, :index 0} {:class :unit, :index 2}]} 
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:class :property, :index 4} 
            {:class :amount, :index 3} 
            {:class :amount, :index 0} 
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]}
        ]} 
        {:class :property, :index 2} 
        {:class :property, :index 2} 
        {:gen 1, :sequence [
            {:class :transform, :index 3} 
            {:class :transform, :index 0} 
            {:class :transform, :index 0} 
            {:gen 0, :sequence [{:class :unit, :index 3} {:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :amount, :index 4} {:class :transform, :index 0} {:class :unit, :index 2}]} 
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:gen 0, :sequence [{:class :unit, :index 3} {:class :transform, :index 0} {:class :amount, :index 3} {:class :amount, :index 4} {:class :amount, :index 4} {:class :transform, :index 0} {:class :unit, :index 2}]} 
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]}
        ]}
    ]} 
    {:class :property, :index 3} 
    {:class :unit, :index 4} 
    {:gen 2, :sequence [
        {:class :amount, :index 2} 
        {:class :property, :index 3} 
        {:gen 1, :sequence [
            {:class :transform, :index 0} 
            {:class :transform, :index 0} 
            {:gen 0, :sequence [{:class :unit, :index 3} {:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :amount, :index 4} {:class :transform, :index 0} {:class :unit, :index 2}]} 
            {:class :amount, :index 0} 
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:gen 0, :sequence [{:class :unit, :index 3} {:class :transform, :index 0} {:class :amount, :index 3} {:class :amount, :index 4} {:class :amount, :index 4} {:class :transform, :index 0} {:class :unit, :index 2}]} 
            {:gen 0, :sequence [{:class :unit, :index 3} {:class :transform, :index 0} {:class :amount, :index 3} {:class :amount, :index 4} {:class :amount, :index 4} {:class :transform, :index 0} {:class :unit, :index 2}]} 
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]}
        ]} 
        {:class :property, :index 2} 
        {:class :property, :index 2} 
        {:gen 1, :sequence [
            {:class :transform, :index 3} 
            {:class :transform, :index 0} 
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]}
        ]} 
        {:class :property, :index 2} 
        {:gen 1, :sequence [
            {:class :transform, :index 3} 
            {:class :transform, :index 0} 
            {:class :transform, :index 0} 
            {:gen 0, :sequence [{:class :unit, :index 3} {:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :amount, :index 4} {:class :transform, :index 0} {:class :unit, :index 2}]} 
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:class :property, :index 4} 
            {:class :amount, :index 3} 
            {:class :amount, :index 0} 
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]} 
            {:class :unit, :index 1}
        ]} 
        {:class :property, :index 2} 
        {:class :property, :index 2} 
        {:gen 1, :sequence [
            {:class :transform, :index 3} 
            {:class :transform, :index 0} 
            {:class :transform, :index 0} 
            {:gen 0, :sequence [{:class :unit, :index 3} {:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :amount, :index 4} {:class :transform, :index 0} {:class :unit, :index 2}]} 
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]}
        ]}
    ]} 
    {:class :property, :index 3} 
    {:class :unit, :index 4} 
    {:gen 2, :sequence [
        {:class :property, :index 0} 
        {:class :property, :index 3} 
        {:gen 1, :sequence [
            {:class :transform, :index 3} 
            {:class :transform, :index 0} 
            {:gen 0, :sequence [{:class :unit, :index 3} {:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :amount, :index 4} {:class :transform, :index 0} {:class :unit, :index 2}]} 
            {:class :amount, :index 0} 
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:gen 0, :sequence [{:class :unit, :index 3} {:class :transform, :index 0} {:class :amount, :index 3} {:class :amount, :index 4} {:class :amount, :index 4} {:class :transform, :index 0} {:class :unit, :index 2}]} 
            {:gen 0, :sequence [{:class :unit, :index 3} {:class :transform, :index 0} {:class :amount, :index 3} {:class :amount, :index 4} {:class :amount, :index 4} {:class :transform, :index 0} {:class :unit, :index 2}]} 
            {:class :property, :index 4} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]}
        ]} 
        {:class :property, :index 2} 
        {:gen 1, :sequence [
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]}
        ]} 
        {:class :property, :index 2} 
        {:class :transform, :index 3} 
        {:gen 1, :sequence [
            {:class :transform, :index 3} 
            {:class :transform, :index 3} 
            {:class :transform, :index 0} 
            {:class :transform, :index 0} 
            {:class :unit, :index 2} 
            {:class :property, :index 4} 
            {:class :property, :index 4} 
            {:class :amount, :index 3} 
            {:class :amount, :index 0} 
            {:class :amount, :index 0} 
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:class :property, :index 4} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]}
        ]} 
        {:class :unit, :index 0} 
        {:gen 1, :sequence [
            {:class :transform, :index 0} 
            {:class :transform, :index 0} 
            {:gen 0, :sequence [{:class :unit, :index 3} {:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :amount, :index 4} {:class :transform, :index 0} {:class :unit, :index 2}]} 
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:gen 0, :sequence [{:class :unit, :index 3} {:class :transform, :index 0} {:class :amount, :index 3} {:class :amount, :index 4} {:class :amount, :index 4} {:class :transform, :index 0} {:class :unit, :index 2}]} 
            {:gen 0, :sequence [{:class :unit, :index 3} {:class :transform, :index 0} {:class :amount, :index 3} {:class :amount, :index 4} {:class :amount, :index 4} {:class :transform, :index 0} {:class :unit, :index 2}]} 
            {:class :amount, :index 0} 
            {:class :transform, :index 0} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]}
        ]}
    ]} 
    {:class :property, :index 3} 
    {:class :unit, :index 4} 
    {:gen 2, :sequence [
        {:class :property, :index 0} 
        {:class :property, :index 0} 
        {:class :property, :index 3} 
        {:gen 1, :sequence [
            {:class :transform, :index 3} 
            {:class :transform, :index 0} 
            {:gen 0, :sequence [{:class :unit, :index 3} {:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :amount, :index 4} {:class :transform, :index 0} {:class :unit, :index 2}]} 
            {:class :amount, :index 0} 
            {:class :transform, :index 1} 
            {:gen 0, :sequence [{:class :unit, :index 3} {:class :transform, :index 0} {:class :amount, :index 3} {:class :amount, :index 4} {:class :amount, :index 4} {:class :transform, :index 0} {:class :unit, :index 2}]} 
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:class :amount, :index 3}
        ]} 
        {:class :property, :index 2} 
        {:gen 1, :sequence [
            {:class :transform, :index 3} 
            {:class :transform, :index 0} 
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:class :property, :index 4} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]}
        ]} 
        {:class :property, :index 2} 
        {:class :property, :index 2} 
        {:gen 1, :sequence [
            {:class :property, :index 0} 
            {:class :transform, :index 0} 
            {:gen 0, :sequence [{:class :unit, :index 3} {:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :amount, :index 4} {:class :transform, :index 0} {:class :unit, :index 2}]} 
            {:class :amount, :index 0} 
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:class :property, :index 4} 
            {:class :property, :index 3} 
            {:class :property, :index 2} 
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]}
        ]} 
        {:class :property, :index 2} 
        {:class :property, :index 2} 
        {:gen 1, :sequence [
            {:class :transform, :index 0} 
            {:class :transform, :index 0} 
            {:gen 0, :sequence [{:class :unit, :index 3} {:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :amount, :index 4} {:class :transform, :index 0} {:class :unit, :index 2}]} 
            {:class :property, :index 4} 
            {:class :amount, :index 0} 
            {:class :property, :index 4} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]} 
            {:gen 0, :sequence [{:class :amount, :index 2} {:class :unit, :index 1} {:class :amount, :index 1} {:class :transform, :index 0} {:class :transform, :index 0} {:class :amount, :index 4} {:class :unit, :index 1} {:class :unit, :index 2}]}
        ]}
    ]}
]})