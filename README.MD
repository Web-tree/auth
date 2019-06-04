[![Build Status](https://travis-ci.org/Web-tree/auth.svg?branch=master)](https://travis-ci.org/Web-tree/auth)

Current module provide basic authentication.

For now its support tho methods: registration and login.

Integration moments:
1) import in maven project current dependency

 <dependency>
     <groupId>org.webtree.auth</groupId>
     <artifactId>core</artifactId>
     <version>{current.version}</version>
  </dependency> 
  
2) You need to create an implementation of AuthRepository
3) You need to create an implementation of WtUserDetails 
4) Configure in application.properties or in application.yaml current properties:

       auth.encoder.password
       auth.encoder.salt
       auth.user-details-class
       auth.jwt.secret
       
