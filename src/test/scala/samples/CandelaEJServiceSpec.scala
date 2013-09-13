package com.candela

import org.junit.runner.RunWith
import org.specs2.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CandelaEJServiceSpec extends JaxRsSpecification { def is =
    sequential  ^
      "Retrieve existing all catalogs"                             ^ e1^
      "Retrieve calculate fuel in the empty "                      ^ e2^
      "Sending first revision and retrieve new calculate fuel"     ^ e3^
      "Sending transaction and retrieve calculate fuel"            ^ e4^
      "Sendding disable transaction and retrieve calculate fuel"   ^ e5^
      "Sending open and close Session"                             ^ e6^
      "Sending bad revision to remote server"                      ^ e7^
      "Sending normal revision to remote server"                   ^ e8^
      "Sending acceptance fuel massa to remote server"             ^ e9^
      "Sending acceptance fuel volume to remote server"            ^ e10^
    end

    def e1 =
      "Retrieve existing all dispensers"                                        ^ br^
      "Given REST client for application deployed at ${http://localhost:5100}"  ^ client^
      "When I do GET to ${journalservice/00000000f18f8617/dispensers}"          ^ get^
      "Then I expect HTTP code ${200}"                                          ^ expectResponseCode^
      "And content to contain ${JSON}"                                          ^ expectResponseContent(
        """
          [
            {
              "number":"1",
              "codeFuel":"ДТ",
              "idServer":"51"
            }
          ]
        """
      )^
      endp^
      "Retrieve existing all employees"                                         ^ br^
      "Given REST client for application deployed at ${http://localhost:5100}"  ^ client^
      "When I do GET to ${journalservice/00000000f18f8617/employees}"           ^ get^
      "Then I expect HTTP code ${200}"                                          ^ expectResponseCode^
      "And content to contain ${JSON}"                                          ^ expectResponseContent(
        """
          [
            {
              "firstName":"Петр",
              "lastName":"Семенов",
              "midleName":"Иванович",
              "keyNumber":"081,34464",
              "active":1,
              "refueler":1
            }
          ]
        """
      )^
      endp^
      "Retrieve existing all tanks"                                             ^ br^
      "Given REST client for application deployed at ${http://localhost:5100}"  ^ client^
      "When I do GET to ${journalservice/00000000f18f8617/tanks}"               ^ get^
      "Then I expect HTTP code ${200}"                                          ^ expectResponseCode^
      "And content to contain ${JSON}"                                          ^ expectResponseContent(
        """
          [
            {
              "number":"1",
              "idServer":"50"
            }
          ]
        """
      )^
      endp^
      end

    def e2 =
      "Retrieve existing initial estimated remaining fuel"                      ^ br^
      "Given REST client for application deployed at ${http://localhost:5100}"  ^ client^
      "When I do GET to ${journalservice/00000000f18f8617/rest}"                ^ get^
      "Then I expect HTTP code ${200}"                                          ^ expectResponseCode^
      "And content to contain ${JSON}"                                          ^ expectResponseContent(
        """
          [
            {
              "codeFuel":"ДТ",
              "volume":0.0,
              "lastIdTransaction":0
            }
          ]
        """
      )^
      endp^
      end

    def e3 =
        "Sending revision to remote server"                                     ^ br^
        "Given REST client for application deployed at ${http://localhost:5100}"^ client^
        "When I do PUT to ${journalservice/00000000f18f8617/revision}"          ^ put(
          """
            [ {
              "data" :"081,34464|да|1|1234|tank|1|density|234|fuel|2391|temperature|234|water|0|seal|42342",
              "dateTime" : "1378379542",
              "id" : "1",
              "lastTransaction" : "0"
            } ]
          """
        )^
        "Then I expect HTTP code ${201}"                                        ^ expectResponseCode^
      endp^
        "Retrieve existing initial estimated remaining fuel"                    ^ br^
        "Given REST client for application deployed at ${http://localhost:5100}"^ client^
        "When I do GET to ${journalservice/00000000f18f8617/rest}"              ^ get^
        "Then I expect HTTP code ${200}"                                        ^ expectResponseCode^
        "And content to contain ${JSON}"                                        ^ expectResponseContent(
          """
            [
              {
                "codeFuel":"ДТ",
                "volume":2391.0,
                "lastIdTransaction":0
              }
            ]
          """
        )^
      endp^
      end

    def e4 =
        "Sending fuelling to remote server"                                     ^ br^
        "Given REST client for application deployed at ${http://localhost:5100}"^ client^
        "When I do PUT to ${journalservice/00000000f18f8617/trans}"             ^ put(
          """
          [ 
            { 
              "RFIDKey" : "081,34464", 
              "dateTime" : 1378782914, 
              "id" : 1, 
              "idDispenser" : 51, 
              "state" : true, 
              "volumeFuel" : 100 
            } 
          ]
          """
        )^
        "Then I expect HTTP code ${201}"                                        ^ expectResponseCode^
      endp^
        "Retrieve existing initial estimated remaining fuel"                    ^ br^
        "Given REST client for application deployed at ${http://localhost:5100}"^ client^
        "When I do GET to ${journalservice/00000000f18f8617/rest}"              ^ get^
        "Then I expect HTTP code ${200}"                                        ^ expectResponseCode^
        "And content to contain ${JSON}"                                        ^ expectResponseContent(
          """
            [
              {
                "codeFuel":"ДТ",
                "volume":2291.0,
                "lastIdTransaction":1
              }
            ]
          """
        )^
      endp^
        "Sending fuelling to remote server"                                     ^ br^
        "Given REST client for application deployed at ${http://localhost:5100}"^ client^
        "When I do PUT to ${journalservice/00000000f18f8617/trans}"             ^ put(
          """
          [ 
            { 
              "RFIDKey" : "081,34464", 
              "dateTime" : 1378782914, 
              "id" : 2, 
              "idDispenser" : 51, 
              "state" : true, 
              "volumeFuel" : 100 
            } 
          ]
          """
        )^
        "Then I expect HTTP code ${201}"                                        ^ expectResponseCode^
      endp^
        "Retrieve existing initial estimated remaining fuel"                    ^ br^
        "Given REST client for application deployed at ${http://localhost:5100}"^ client^
        "When I do GET to ${journalservice/00000000f18f8617/rest}"              ^ get^
        "Then I expect HTTP code ${200}"                                        ^ expectResponseCode^
        "And content to contain ${JSON}"                                        ^ expectResponseContent(
          """
            [
              {
                "codeFuel":"ДТ",
                "volume":2191.0,
                "lastIdTransaction":2
              }
            ]
          """
        )^
      endp^
      end

    def e5 =
        "Sending fuelling to remote server"                                     ^ br^
        "Given REST client for application deployed at ${http://localhost:5100}"^ client^
        "When I do PUT to ${journalservice/00000000f18f8617/trans}"             ^ put(
          """
          [ 
            { 
              "RFIDKey" : "081,34464", 
              "dateTime" : 1378782914, 
              "id" : 2, 
              "idDispenser" : 51, 
              "state" : false, 
              "volumeFuel" : 100 
            } 
          ]
          """
        )^
        "Then I expect HTTP code ${201}"                                        ^ expectResponseCode^
    endp^
      "Retrieve existing initial estimated remaining fuel"                      ^ br^
      "Given REST client for application deployed at ${http://localhost:5100}"  ^ client^
      "When I do GET to ${journalservice/00000000f18f8617/rest}"                ^ get^
      "Then I expect HTTP code ${200}"                                          ^ expectResponseCode^
      "And content to contain ${JSON}"                                          ^ expectResponseContent(
        """
          [
            {
              "codeFuel":"ДТ",
              "volume":2291.0,
              "lastIdTransaction":2
            }
          ]
        """
      )^
    endp^
    end
    def e6 =
        "Sending open session to remote server"                                 ^ br^
        "Given REST client for application deployed at ${http://localhost:5100}"^ client^
        "When I do PUT to ${journalservice/00000000f18f8617/session}"           ^ put(
          """
          [ 
            { 
              "data" : "да|1|1221", 
              "dateTime" : 1378965926, 
              "event" : "open", 
              "id" : 1, 
              "lastIdTransaction" : 2, 
              "rfidKey" : "081,34464" 
            } 
          ]
          """
        )^
        "Then I expect HTTP code ${201}"                                        ^ expectResponseCode^
    endp^
        "Sending close session to remote server"                                ^ br^
        "Given REST client for application deployed at ${http://localhost:5100}"^ client^
        "When I do PUT to ${journalservice/00000000f18f8617/session}"           ^ put(
          """
          [ 
            { 
              "data" : "да|1|1221", 
              "dateTime" : 1378966926, 
              "event" : "close", 
              "id" : 2, 
              "lastIdTransaction" : 2, 
              "rfidKey" : "081,34464" 
            } 
          ]
          """
        )^
        "Then I expect HTTP code ${201}"                                        ^ expectResponseCode^
    endp^
    end

    def e7 =
        "Sending bad revision to remote server"                                     ^ br^
        "Given REST client for application deployed at ${http://localhost:5100}"^ client^
        "When I do PUT to ${journalservice/00000000f18f8617/revision}"          ^ put(
          """
            [ {
              "data" :"081,34464|да|1|1234|tank|1|density|234|fuel|5391|temperature|234|water|0|seal|42342",
              "dateTime" : "1378967926", 
              "id" : "2",
              "lastTransaction" : "2"
            } ]
          """
        )^
        "Then I expect HTTP code ${201}"                                        ^ expectResponseCode^
      endp^
        "Retrieve existing initial estimated remaining fuel"                    ^ br^
        "Given REST client for application deployed at ${http://localhost:5100}"^ client^
        "When I do GET to ${journalservice/00000000f18f8617/rest}"              ^ get^
        "Then I expect HTTP code ${200}"                                        ^ expectResponseCode^
        "And content to contain ${JSON}"                                        ^ expectResponseContent(
          """
            [
              {
                "codeFuel":"ДТ",
                "volume":3000.0,
                "lastIdTransaction":2
              }
            ]
          """
        )^
      endp^
      end
    
    def e8 =
        "Sending normal revision to remote server"                                     ^ br^
        "Given REST client for application deployed at ${http://localhost:5100}"^ client^
        "When I do PUT to ${journalservice/00000000f18f8617/revision}"          ^ put(
          """
            [ {
              "data" :"081,34464|да|1|1234|tank|1|density|234|fuel|500|temperature|234|water|0|seal|42342",
              "dateTime" : "1378967996", 
              "id" : "3",
              "lastTransaction" : "2"
            } ]
          """
        )^
        "Then I expect HTTP code ${201}"                                        ^ expectResponseCode^
      endp^
        "Retrieve existing initial estimated remaining fuel"                    ^ br^
        "Given REST client for application deployed at ${http://localhost:5100}"^ client^
        "When I do GET to ${journalservice/00000000f18f8617/rest}"              ^ get^
        "Then I expect HTTP code ${200}"                                        ^ expectResponseCode^
        "And content to contain ${JSON}"                                        ^ expectResponseContent(
          """
            [
              {
                "codeFuel":"ДТ",
                "volume":500.0,
                "lastIdTransaction":2
              }
            ]
          """
        )^
      endp^
      end

    def e9 =
        "Sending acceptance fule massa to remote server"                        ^ br^
        "Given REST client for application deployed at ${http://localhost:5100}"^ client^
        "When I do PUT to ${journalservice/00000000f18f8617/acceptance}"          ^ put(
          """
            [ 
              { 
                "data" : "ДТ|43234-ВВВ|АА8765УФ|massa|1000|1200|нет|23|860|1231313|numberTank|1|levelFuelStart|500|levelFuelFinal|1662|numberSeal|213123",
                "dateTime" :  1378970827, 
                "id" : 1, 
                "lastIdTransaction" : 2 
              }
            ]
          """
        )^
        "Then I expect HTTP code ${201}"                                        ^ expectResponseCode^
      endp^
        "Retrieve existing initial estimated remaining fuel"                    ^ br^
        "Given REST client for application deployed at ${http://localhost:5100}"^ client^
        "When I do GET to ${journalservice/00000000f18f8617/rest}"              ^ get^
        "Then I expect HTTP code ${200}"                                        ^ expectResponseCode^
        "And content to contain ${JSON}"                                        ^ expectResponseContent(
          """
            [
              {
                "codeFuel":"ДТ",
                "volume":1662.0,
                "lastIdTransaction":2
              }
            ]
          """
        )^
      endp^
      end
    def e10 =
        "Sending acceptance fule massa to remote server"                        ^ br^
        "Given REST client for application deployed at ${http://localhost:5100}"^ client^
        "When I do PUT to ${journalservice/00000000f18f8617/acceptance}"          ^ put(
          """
            [ 
              { 
                "data" : "ДТ|43234-ВВВ|АА8765УФ|volume|300|1200|нет|23|860|1231313|numberTank|1|levelFuelStart|1662|levelFuelFinal|1962|numberSeal|213123",
                "dateTime" :  1378970850, 
                "id" : 2, 
                "lastIdTransaction" : 2 
              }
            ]
          """
        )^
        "Then I expect HTTP code ${201}"                                        ^ expectResponseCode^
      endp^
        "Retrieve existing initial estimated remaining fuel"                    ^ br^
        "Given REST client for application deployed at ${http://localhost:5100}"^ client^
        "When I do GET to ${journalservice/00000000f18f8617/rest}"              ^ get^
        "Then I expect HTTP code ${200}"                                        ^ expectResponseCode^
        "And content to contain ${JSON}"                                        ^ expectResponseContent(
          """
            [
              {
                "codeFuel":"ДТ",
                "volume":1962.0,
                "lastIdTransaction":2
              }
            ]
          """
        )^
      endp^
      end
}
