package com.brightcoding.app.ws.security;

public class SecurityConstants {
//en utilise static lorsque on veux acceder un attribut sans intansier un objet
	public static final long EXPIRATION_TIME = 864000000;//on utilise final pour ne pas hériter cette attribut
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users";
	public static final String TOKEN_SECRET = "296e048f-20fb-4ffb-bc37-f8da8de972ac";
}
