(ns user
  (:require [clojure.tools.namespace.repl :refer [refresh]]
            [clojure.java.io :as io]
            [clojure.data.json :as json]

            [org.httpkit.client :as http]

            [literate.client.core :as literate]))

(comment

  (refresh)


  (def view (partial literate/view {:url "http://localhost:8118"}))

  ;; -- Vega Lite.

  (view
    (literate/vega-lite
      {"$schema" "https://vega.github.io/schema/vega-lite/v4.json"
       :description "A simple bar chart with embedded data."
       :data {:values
              [{:a "A" :b 28}
               {:a "B" :b 55}
               {:a "C" :b 43}
               {:a "D" :b 91}
               {:a "E" :b 81}
               {:a "F" :b 53}
               {:a "G" :b 19}
               {:a "H" :b 87}
               {:a "I" :b 52}]}
       :mark "bar"
       :encoding {:x {:field "a"
                      :type "ordinal"}
                  :y {:field "b"
                      :type "quantitative"}}}))

  ;; -- Code.

  (view (literate/code (slurp (io/resource "literate/client/core.clj"))))


  ;; -- Leaflet.

  (view (literate/leaflet {}))

  (def geojson (json/read (io/reader "src/dev/resources/points.geojson")))

  (def sample (update geojson "features" #(take 10 %)))

  (def center (vec (reverse (get-in geojson ["features" 0 "geometry" "coordinates"]))))

  (def leaflet-widget
    (literate/leaflet
      {:style {:height "600px"}
       :center center
       :zoom 10
       :geojson (update geojson "features" #(take 10 %))}))

  (view leaflet-widget)

  (view (merge leaflet-widget {:widget/geojson (update geojson "features" #(take 1 %))}))


  ;; -- Markdown.

  (view (literate/markdown "**Welcome to Literate**\n\nEval some forms to get started!"))


  ;; -- HTML.

  (view
    (literate/html
      (rum.server-render/render-static-markup
        [:div.bg-white.p-3
         [:h1.text-6xl "Hello from Hiccup"]
         [:span "Text"]])))


  ;; -- Row.

  (view
    (literate/row
      {}
      (literate/leaflet {})
      (literate/vega-lite
        {"$schema" "https://vega.github.io/schema/vega-lite/v4.json"
         :description "A simple bar chart with embedded data."
         :data {:url "https://vega.github.io/editor/data/stocks.csv"}
         :transform [{"filter" "datum.symbol==='GOOG'"}]
         :mark "line"
         :encoding {:x {:field "date"
                        :type "temporal"}
                    :y {:field "price"
                        :type "quantitative"}}})))


  ;; -- Column.

  (view
    (literate/column
      {}
      (literate/leaflet {})
      (literate/vega-lite
        {"$schema" "https://vega.github.io/schema/vega-lite/v4.json"
         :description "A simple bar chart with embedded data."
         :data {:url "https://vega.github.io/editor/data/stocks.csv"}
         :transform [{"filter" "datum.symbol==='GOOG'"}]
         :mark "line"
         :encoding {:x {:field "date"
                        :type "temporal"}
                    :y {:field "price"
                        :type "quantitative"}}})))


  ;; -- Welcome.

  (view
    (literate/column
      {}
      (literate/hiccup
        [:div.flex.flex-col.space-y-3.bg-white.p-3.font-light
         [:h1.text-3xl
          {:style {:font-family "Cinzel"}}
          "Welcome to Literate"]

         [:p.font-semibold
          "Literate is a Clojure & ClojureScript application which you can use to visualize data."]

         [:p.mt-4
          "This interface that you're looking at it's called a " [:span.font-bold "Widget"]
          ", and you can create one from a Clojure REPL."]

         [:p.mt-2.mb1 "There are a few different types of Widgets that are supported:"]

         [:ul.list-disc.list-inside.ml-2
          [:li "Code"]
          [:li "Markdown"]
          [:li "Hiccup"]
          [:li "Vega Lite"]
          [:li "Leaflet"]
          [:li "Column layout"]
          [:li "Row layout"]]])

      (literate/hiccup
        [:span.p-2.text-lg "Vega Lite Widget"])

      (literate/vega-lite
        {"$schema" "https://vega.github.io/schema/vega-lite/v4.json"
         :description "A simple bar chart with embedded data."
         :data {:url "https://vega.github.io/editor/data/stocks.csv"}
         :transform [{"filter" "datum.symbol==='GOOG'"}]
         :mark "line"
         :encoding {:x {:field "date"
                        :type "temporal"}
                    :y {:field "price"
                        :type "quantitative"}}})

      (literate/hiccup
        [:span.p-2.text-lg "Code Widget"])

      (literate/code (slurp (io/resource "literate/client/core.clj")))

      (literate/hiccup
        [:span.p-2.text-lg "Leaflet Widget"])

      (literate/leaflet {:style {:height "400px"}
                         :center [51.505 -0.09]
                         :zoom 10})))

  )