




IF EXISTS(SELECT *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'fnColumnExists') AND type IN (N'FN', N'IF', N'TF', N'FS', N'FT') )
  DROP FUNCTION fnColumnExists
GO

CREATE FUNCTION fnColumnExists
(
	 @tableName  varchar(100),
	 @columnName varchar(100)

)
RETURNS int
AS
BEGIN

	    DECLARE @exist int

        IF EXISTS(SELECT * FROM sys.columns
            WHERE Name = @columnName AND OBJECT_ID = OBJECT_ID(@tableName))
            BEGIN
                    set @exist = 1
            END
        ELSE
            BEGIN
                    set @exist = 0
            END

	RETURN @exist

END

GO






IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'tblUsers') AND type IN (N'U'))
BEGIN
    CREATE TABLE tblUsers
    (
        id                      BIGINT      IDENTITY (1, 1) PRIMARY KEY,
        firstName              VARCHAR(100) NOT NULL,
        lastName             VARCHAR(100) NOT NULL,
        username                VARCHAR(70) NOT NULL,
        password                VARCHAR(200)  NOT NULL,
        phoneNumber            VARCHAR(20) NOT NULL,
        email                   VARCHAR(100)  NULL ,

        hospitalId            varchar(50) DEFAULT 0,
        category              varchar (1) NOT NULL,
        address              VARCHAR(MAX) NULL,
        roleId               BIGINT ,

        status                  INT         NOT NULL DEFAULT 0,
        loginAttempts          BIGINT NOT NULL DEFAULT 0,
        lastLogin              DATETIME NULL,
        lockedDate             DATETIME NULL,
        failedLoginDate       DATETIME NULL,
        accountCreatedOn      DATETIME NOT NULL DEFAULT GETDATE(),
        passwordChangedOn     DATETIME NULL,
        createdOn            DATETIME     NOT NULL DEFAULT GETDATE(),
        createdBy            BIGINT NOT NULL,
       -- createdBy            BIGINT NOT NULL REFERENCES tblusers (id),
        activationCode     varchar(200),
        codeExpiry         DATETIME NOT NULL DEFAULT DateADD(mi, 15, GETDATE()),
        lastUpdatedOn        DATETIME     NULL,
        lastUpdatedBy        BIGINT     NULL,

        approved             BIT DEFAULT 0

      )

    CREATE NONCLUSTERED  INDEX IX_tblUsers ON tblUsers (username,email,phoneNumber);
    PRINT 'User Table created successfully'

END
ELSE
BEGIN
    PRINT 'User table already exist....'
END
GO


INSERT INTO tblusers (firstName,lastName,username,password,address,phoneNumber,email,category,createdBy)
    VALUES ('testuser','testuser2','testuser','testpassword','Lagos','08137729363','tst@isw.com','S',0)
GO











IF NOT EXISTS(SELECT *
              FROM sys.objects
              WHERE object_id = OBJECT_ID(N'previousPasswords') AND type IN (N'U'))
  CREATE TABLE previousPasswords
  (
    id         BIGINT               IDENTITY (1, 1) PRIMARY KEY,
    password   VARCHAR(200) NOT NULL,
    userId    BIGINT      NOT NULL REFERENCES tblUsers (id)
      ON DELETE CASCADE,
    created_on DATETIME NOT NULL DEFAULT GETDATE()
  )

  CREATE NONCLUSTERED  INDEX IX_previous_passwords ON previousPasswords (userId);
    PRINT 'previousPasswords Table created successfully'
GO






//----------------------  PROCEDURES ----------------------------------------



IF EXISTS(SELECT *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'createUser') AND type IN (N'P', N'PC'))
  DROP PROCEDURE createUser

GO

CREATE PROCEDURE [dbo].[createUser]
        @id         BIGINT OUTPUT,

        @firstName              VARCHAR(100) ,
        @lastName             VARCHAR(100) ,
        @username                VARCHAR(70) ,
        @password                VARCHAR(200)  ,
        @phoneNumber            VARCHAR(20) ,
        @email                   VARCHAR(100)   ,
        @hospitalId            varchar(50) ,
        @category              varchar (1),
        @address              VARCHAR(100) ,
        @roleId               BIGINT ,
        @activationCode varchar(200),
        @createdBy  BIGINT


    AS

      BEGIN TRANSACTION
      INSERT INTO tblUsers (firstName,lastName,username,password,phoneNumber,email,hospitalId,
      category,address,roleId,createdBy,activationCode)
      VALUES (@firstName,@lastName,@username,@password,@phoneNumber,@email, @hospitalId,
      @category,@address,@roleId,@createdBy,@activationCode)

      IF (@@ERROR <> 0)
        BEGIN
          ROLLBACK TRANSACTION
          RETURN @@Error
        END

      SELECT @id = SCOPE_IDENTITY();

      INSERT INTO previousPasswords (password, userId) VALUES (@password, @id)

      IF (@@ERROR <> 0)
        BEGIN
          ROLLBACK TRANSACTION
          RETURN @@Error
        END
      COMMIT TRANSACTION

      SELECT @id ;

      RETURN @@Error

GO



IF EXISTS(SELECT *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'isUserExists') AND type IN (N'P', N'PC'))
  DROP PROCEDURE isUserExists
GO
CREATE PROCEDURE isUserExists
	@username VARCHAR(70),
	@email    VARCHAR(70)
AS
	SET NOCOUNT ON
	 SELECT id FROM tblUsers (nolock) WHERE username=@username and email= @email
	RETURN @@Error


GO





IF EXISTS(SELECT *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'getUserByUsername') AND type IN (N'P', N'PC'))
  DROP PROCEDURE getUserByUsername
GO

CREATE PROCEDURE getUserByUsername
    @username VARCHAR(70)
AS
    SELECT
     id, firstName,lastName,username,password,phoneNumber,email,hospitalId,
      category,address,roleId,createdBy,status,activationCode
    FROM   tblUsers (nolock)
   WHERE username  = @username or email = @username

   RETURN @@Error

GO




IF EXISTS(SELECT *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'getUserPreviousPasswords') AND type IN (N'P', N'PC'))
  DROP PROCEDURE getUserPreviousPasswords

GO

CREATE PROCEDURE getUserPreviousPasswords
    @userId BIGINT
AS
  SET NOCOUNT ON

  SELECT TOP 4 p.password
  FROM previousPasswords p  (nolock) INNER JOIN tblUsers u (nolock) ON p.userId = u.id
  WHERE  u.id = @userId
  ORDER BY p.created_on DESC

  RETURN @@Error
GO






IF EXISTS(SELECT *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'activateAccount') AND type IN (N'P', N'PC'))
  DROP PROCEDURE activateAccount
GO

CREATE PROCEDURE activateAccount
	@activationCode VARCHAR(200),
	@username VARCHAR(70)

AS
	SET NOCOUNT ON

    	  DECLARE @expired            int
    	  DECLARE @activate           varchar(1)

          SELECT @expired= DATEDIFF(MINUTE, codeExpiry, GETDATE())
          FROM tblUsers (nolock)
          WHERE activationCode=@activationCode and username= @username

         if ( @expired < 15 )
            BEGIN

    			 UPDATE tblUsers SET status =1
                 where activationCode=@activationCode and username= @username

                 set @activate = 1

          END
         ELSE
            BEGIN
                  set @activate = 0
            END

         SELECT @activate as activate

    	RETURN @@Error
GO




IF EXISTS(SELECT *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'changeUserPassword') AND type IN (N'P', N'PC'))
  DROP PROCEDURE changeUserPassword
GO


CREATE PROCEDURE changeUserPassword
    @password VARCHAR(200),
    @userId  BIGINT OUTPUT
AS
  SET NOCOUNT ON


  BEGIN TRANSACTION

  UPDATE tblUsers
  SET
    password            = @password,
    status              = 1,
    lockedDate         = NULL,
    passwordChangedOn = GETDATE()
  WHERE id = @userId

  IF (@@ERROR <> 0)
    BEGIN
      ROLLBACK TRANSACTION
      RETURN @@Error
    END

  INSERT INTO previousPasswords (password, userId) VALUES (@password, @userId)

  IF (@@ERROR <> 0)
    BEGIN
      ROLLBACK TRANSACTION
      RETURN @@Error
    END
  COMMIT TRANSACTION

  SELECT @userId;

  RETURN @@Error
GO







IF EXISTS(SELECT *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'loginMerchantUser') AND type IN (N'P', N'PC'))
  DROP PROCEDURE loginMerchantUser
GO


CREATE PROCEDURE loginMerchantUser
    @username VARCHAR(70),
    @hospitalId VARCHAR(50)
AS
  SET NOCOUNT ON
    DECLARE @password_expired                   BIT = 0
    DECLARE @password_expiration_days_remaining INT = -1

    SELECT @password_expiration_days_remaining = 90 - DATEDIFF(DAY, u.passwordChangedOn, GETDATE())
    FROM tblUsers u (nolock)
     WHERE (u.email = @username OR u.username = @username) and hospitalId = @hospitalId

    IF (@password_expiration_days_remaining >= 0)
     		BEGIN
     		 SET @password_expired = 0
    	   END
       ELSE
     		BEGIN
     		  SET @password_expired = 1
     	    END

      SELECT
               u.*,
               @password_expired                   AS password_expired,
               ISNULL(@password_expiration_days_remaining,-1) AS password_expiration_days_remaining
              FROM tblUsers u (nolock)
              WHERE   (u.email = @username OR u.username = @username) and hospitalId = @hospitalId

  RETURN @@Error

GO




IF EXISTS(SELECT *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'loginLock') AND type IN (N'P', N'PC'))
  DROP PROCEDURE loginLock
GO

CREATE PROCEDURE loginLock
	@userId  BIGINT

AS
	SET NOCOUNT ON
	 update tblUsers set lockedDate = GETDATE() WHERE id=@userId

RETURN @@Error

GO




IF EXISTS(SELECT *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'updateFailedLogin') AND type IN (N'P', N'PC'))
  DROP PROCEDURE updateFailedLogin
GO
CREATE PROCEDURE updateFailedLogin
	@username  varchar(70)
AS
	 SET NOCOUNT ON
	 DECLARE @userExist BIGINT
	 DECLARE @loginCount BIGINT

	 SELECT @userExist = COUNT(id),@loginCount =loginAttempts  from tblUsers
	 where username = @username or email = @username
	 group by loginAttempts

	 if (@userExist > 0)
         begin
            update tblUsers set failedLoginDate = GETDATE(),loginAttempts = @loginCount + 1
            WHERE username  = @username or email = @username
         end

RETURN @@Error

GO


IF EXISTS(SELECT *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'updateLogin') AND type IN (N'P', N'PC'))
  DROP PROCEDURE updateLogin
GO

CREATE PROCEDURE updateLogin
    @username         VARCHAR(70),
    @passwordMatched BIT,
    @loginAttemptsConfig  INT
AS


  DECLARE @loginAttempts INT
  DECLARE @lockedDate    DATETIME


  SELECT @loginAttempts = loginAttempts
  FROM tblUsers u (nolock)
  WHERE  u.username = @username or email = @username

  IF (@passwordMatched = 0)
    BEGIN
      UPDATE tblUsers
      SET loginAttempts = @loginAttempts + 1, failedLoginDate = GETDATE()
      WHERE  username = @username
      IF (@loginAttempts >= @loginAttemptsConfig)
        BEGIN
          UPDATE tblUsers
          SET lockedDate = GETDATE()
          WHERE username = @username or email = @username
        END
      RETURN
    END

  IF (@passwordMatched = 1)
    BEGIN
      UPDATE tblUsers
      SET lockedDate = NULL, loginAttempts = 0, lastLogin = GETDATE()
      WHERE username  = @username or email = @username
    END

  RETURN @@Error

GO





--------------------------  new procedures for 4/03/2019 ---------------------------------------------

IF EXISTS(SELECT *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'getUserByParameter') AND type IN (N'P', N'PC'))
  DROP PROCEDURE getUserByParameter
GO

CREATE PROCEDURE getUserByParameter
    @username VARCHAR(70)
AS
    SELECT
     id, firstName,lastName,username,password,phoneNumber,email,hospitalId,
      category,address,roleId,createdBy,status,activationCode
    FROM   tblUsers (nolock)
   WHERE  email = @username

   RETURN @@Error

GO



IF EXISTS(SELECT *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'updateAccountCode') AND type IN (N'P', N'PC'))
  DROP PROCEDURE updateAccountCode
GO

CREATE PROCEDURE updateAccountCode
    @userId    BIGINT,
    @code      varchar(200)
AS
  SET NOCOUNT ON

  UPDATE tblUsers
  SET activationCode=@code, codeExpiry= DateADD(mi, 15, GETDATE())
  where  id= @userId

  RETURN @@Error

GO




IF EXISTS(SELECT *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'loginUnlock') AND type IN (N'P', N'PC'))
  DROP PROCEDURE loginUnlock
GO

CREATE PROCEDURE loginUnlock
	@userId  BIGINT

AS
	SET NOCOUNT ON
	 update tblUsers set lockedDate = null, loginAttempts=0
	 WHERE id=@userId

RETURN @@Error

GO








IF EXISTS(SELECT *
             FROM sys.objects
             WHERE object_id = OBJECT_ID(N'getUsersCategory') AND type IN (N'P', N'PC'))
     DROP PROCEDURE getUsersCategory
   GO

   CREATE PROCEDURE getUsersCategory
           @page_num  INT = 1,
           @page_size INT = 100,
--           @category varchar(1),
--           @userId  BIGINT
   AS
    SET NOCOUNT ON
       SET @page_num = ABS(@page_num)
       SET @page_size = ABS(@page_size)
       IF @page_num < 1
         SET @page_num = 1
       IF @page_size < 1
         SET @page_size = 1

       SELECT COUNT(*) AS cnt
           FROM tblUsers  (nolock)


       ;WITH pg AS
       (
           SELECT id
           FROM tblUsers  (nolock)

           ORDER BY id
           OFFSET @page_size * (@page_num -1) ROWS
           FETCH NEXT @page_size ROWS ONLY
       )
       SELECT
          a.id,
           firstName,
		   lastName,
		   username,
		   password,
		   phoneNumber,
		   email,
		   hospitalId,
		   (Select name from tblHospitalProfile hp where a.hospitalId=hp.hospitalId) as hospitalName,
         category,
		 address,
		 roleId,
		 (Select name from dbo.Role tr where a.roleId=tr.id) as role,
		 createdBy,
		 (Select firstname from tblUsers tu where a.createdBy=tu.id) as creator           ,
		 createdOn   ,
		 status,
		 approved


        FROM tblUsers  as a (nolock)
        INNER JOIN pg on pg.id = a.id
        ORDER BY pg.id desc

       RETURN @@Error
   GO








IF EXISTS(SELECT *
             FROM sys.objects
             WHERE object_id = OBJECT_ID(N'getHospitalUsers') AND type IN (N'P', N'PC'))
     DROP PROCEDURE getHospitalUsers
   GO

   CREATE PROCEDURE getHospitalUsers

           @page_num  INT = 1,
           @page_size INT = 100,
           @hospitalId varchar(50)

   AS

    SET NOCOUNT ON



       SET @page_num = ABS(@page_num)
       SET @page_size = ABS(@page_size)
       IF @page_num < 1
         SET @page_num = 1
       IF @page_size < 1
         SET @page_size = 1

       SELECT COUNT(*) AS cnt
           FROM tblUsers  (nolock)
           WHERE  hospitalId= @hospitalId

       ;WITH pg AS
       (
           SELECT id
           FROM tblUsers  (nolock)
               WHERE  hospitalId= @hospitalId
           ORDER BY id
           OFFSET @page_size * (@page_num -1) ROWS
           FETCH NEXT @page_size ROWS ONLY
       )
       SELECT
          a.id,
           firstName,lastName,username,password,phoneNumber,email,hospitalId,
         category,address,roleId,createdBy,status,approved,ISNULL(lockedDate,'')
        FROM tblUsers  as a (nolock)
        INNER JOIN pg on pg.id = a.id where hospitalId= @hospitalId
        ORDER BY pg.id desc

       RETURN @@Error
   GO





   IF EXISTS(SELECT *
             FROM sys.objects
             WHERE object_id = OBJECT_ID(N'updateUser') AND type IN (N'P', N'PC'))
     DROP PROCEDURE updateUser
   GO

   CREATE PROCEDURE updateUser
           @id              BIGINT,
           @firstName              VARCHAR(100) ,
           @lastName             VARCHAR(100) ,
           --@username                VARCHAR(70) ,
           --@password                VARCHAR(200)  ,
           @phoneNumber            VARCHAR(20) ,
           @email                   VARCHAR(100)   ,
           @hospitalId            varchar(50) ,
           @category              varchar (1),
           @address              VARCHAR(100) ,
           @roleId               BIGINT ,
           @lastUpdatedBy   BIGINT
      AS
        SET NOCOUNT ON
        UPDATE tblUsers
        SET firstName    = (CASE
                             WHEN @firstName IS NULL THEN firstName
                             ELSE @firstName
                             END),
          lastName       = (CASE
                             WHEN @lastName IS NULL THEN lastName
                             ELSE @lastName
                             END)
                             ,
          phoneNumber       = (CASE
                                WHEN @phoneNumber IS NULL THEN phoneNumber
                                ELSE @phoneNumber
                               END),
          email           = (CASE
                             WHEN @email IS NULL THEN email
                              ELSE @email
                             END),
         hospitalId =  (CASE
                                WHEN @hospitalId IS NULL THEN hospitalId
                                ELSE @hospitalId
                               END) ,
         category=  (CASE
                                WHEN @category IS NULL THEN category
                                ELSE @category
                               END)              ,
         address=  (CASE
                                WHEN @address IS NULL THEN address
                                ELSE @address
                               END) ,
         roleId = (CASE
                                WHEN @roleId IS NULL THEN roleId
                                ELSE @roleId
                               END),
         lastUpdatedBy = @lastUpdatedBy,
         lastUpdatedOn = GETDATE()

         WHERE  id =@id

      RETURN @@Error

   GO


IF EXISTS(SELECT *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'changeUserPassword') AND type IN (N'P', N'PC'))
  DROP PROCEDURE changeUserPassword
GO
CREATE PROCEDURE changeUserPassword
    @password VARCHAR(200),
    @userId  BIGINT OUTPUT
AS
  SET NOCOUNT ON
  BEGIN TRANSACTION

  UPDATE tblUsers
  SET
    password            = @password,
    status              = 1,
    lockedDate         = NULL,
    passwordChangedOn = GETDATE()
  WHERE id = @userId

  IF (@@ERROR <> 0)
    BEGIN
      ROLLBACK TRANSACTION
      RETURN @@Error
    END
  INSERT INTO previousPasswords (password, userId) VALUES (@password, @userId)

  IF (@@ERROR <> 0)
    BEGIN
      ROLLBACK TRANSACTION
      RETURN @@Error
    END
  COMMIT TRANSACTION
  SELECT @userId;

  RETURN @@Error
GO




IF EXISTS(SELECT
            *
          FROM sys.objects
          WHERE object_id = OBJECT_ID(N'getUser') AND type IN (N'P', N'PC'))
  DROP PROCEDURE getUser
GO

CREATE PROCEDURE getUser
	@id BIGINT
AS
	SET NOCOUNT ON

	SELECT  id, firstName,lastName,username,password,phoneNumber,email,hospitalId,
      category,address,roleId,createdBy,status,activationCode
    FROM   tblUsers (nolock)
	WHERE
	id = @id

	RETURN @@Error
GO
