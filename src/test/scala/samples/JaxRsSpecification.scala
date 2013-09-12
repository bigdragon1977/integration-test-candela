package com.candela

import spray.json._
import DefaultJsonProtocol._
import org.glassfish.jersey.client.ClientConfig
import org.specs2.Specification
import org.specs2.specification.Given
import org.specs2.specification.Given.function1ToGiven
import org.specs2.specification.Then
import org.specs2.specification.Then.function1ToThen
import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.ClientRequestFilter
import javax.ws.rs.client.Entity
import javax.ws.rs.core.Response
import javax.ws.rs.core.Form
import org.specs2.specification.When
import javax.ws.rs.core.MediaType



abstract class JaxRsSpecification extends Specification {
  
  var token: String = Base64.encode("login:password"getBytes)

  var client: Given[ Client ] = ( baseUrl: String) =>
     ClientBuilder.newClient( new ClientConfig().property( "baseUrl", baseUrl ))

  var expectResponseContent: Then[ Response ] = (response: Response ) => (content: String ) =>
    response.readEntity( classOf[ String ] ) should contain( content )

  val expectResponseCode: Then[ Response ] = ( response: Response ) => ( code: String ) => 
            response.getStatus() must_== code.toInt

  val expectResponseHeader: Then[ Response ] = ( response: Response ) => ( header: String, value: String ) =>
          response.getHeaderString( header ) should contain( value ) 

  val get: When[ Client, Response ] = ( client: Client ) => ( url: String ) =>  
    client
      .target( s"${client.getConfiguration.getProperty( "baseUrl" )}/$url" )
      .request( MediaType.APPLICATION_JSON )
      .header("Authorization", "Basic "+ token)
      .get( classOf[ Response ] )
  
  def put( json: String ): When[ Client, Response ] = ( client: Client ) => ( url: String ) => 
        client
            .target( s"${client.getConfiguration.getProperty( "baseUrl" )}/$url" )
            .request( MediaType.APPLICATION_JSON )
            .header("Authorization", "Basic "+ token)
            .put( 
             Entity.entity(json , MediaType.APPLICATION_JSON) ,
                classOf[ Response ] 
            )
  def expectResponseContent( json: String ): Then[ Response ] = ( response: Response ) => ( format: String ) => {
        format match { 
            case "JSON" => response.readEntity( classOf[ String ] ).asJson must_== json.asJson
            case _ => response.readEntity( classOf[ String ] ) must_== json
        }
  }

}
