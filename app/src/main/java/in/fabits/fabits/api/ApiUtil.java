package in.fabits.fabits.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ApiUtil {

    private static String mToken;
    private static String mBaseUrl;
    private static String mName;
    private static String mUserId;
    private static String mUserName;
    private static String mProfileSmall;
    private static String mPostUrl;
    private static String mChatListUrl;
    private static String mOnlineListUrl;
    private static String mLikeUrl;
    private static String mLikesUrl;
    private static String mTrendUrl;
    private static String mCommentUrl;
    private static String mFollowing;
    private static String mNewMessage;
    private static String mSimpleMessage;
    private static String mComplexMessage;
    private static String mPostTextUrl;
    private static String mPostImageUrl;
    private static String mReadConversationUrl;
    private static String mNotification;
    private static String mRandomProfiles;
    private static String mSettings;
    private static String mConversationInit;
    private static String mOnline_conversation;

    private static String mPostSingle;
    private static String mSearch;
    private static String mPostsPool;
    private static String mForceReadConversation;
    private static String mTyping;
    private static String mConversationDelete;
    private static String mPostDelete;
    private static String mUnFollowPost;
    private static String mCommentDelete;
    private static String mFollow;
    private static String mChatImageUpload;
    private static String mCheckFaceMatch;

    private static String mChangePassword;
    private static String mContactUs;
    private static String mLogout;
    private static String mChangePhoneNumber;
    private static String mOtp;
    private static String mChatAllow;
    private static String mChatBlock;
    private static String mBlock;
    private static String my_blocks;
    private static String my_blocks_list;
    private static String mBigImage;
    private static String mProfileCountLists;
    private static String mNewNotification;
    private static String mProfilePic;
    private static String mProfileWall;
    private static String mSuggestion;
    private static String mSignUpPassword;
    private static String mSignUpPasswordSkip;
    private static String mSignUpGenderDob;
    private static String mSignUpProfilePicSkip;
    private static String mSignUpProfilePic;

    public static String getSignUpProfilePic() {
        return getBaseUrl() + mSignUpProfilePic;
    }

    public static void setSignUpProfilePic(String signUpProfilePic) {
        mSignUpProfilePic = signUpProfilePic;
    }

    public static String getSignUpPassword() {
        return getBaseUrl() + mSignUpPassword;
    }

    public static void setSignUpPassword(String signUpPassword) {
        mSignUpPassword = signUpPassword;
    }

    public static String getSignUpPasswordSkip() {
        return getBaseUrl() + mSignUpPasswordSkip;
    }

    public static void setSignUpPasswordSkip(String signUpPasswordSkip) {
        mSignUpPasswordSkip = signUpPasswordSkip;
    }

    public static String getSignUpGenderDob() {
        return getBaseUrl() + mSignUpGenderDob;
    }

    public static void setSignUpGenderDob(String signUpGenderDob) {
        mSignUpGenderDob = signUpGenderDob;
    }

    public static String getSignUpProfilePicSkip() {
        return getBaseUrl() + mSignUpProfilePicSkip;
    }

    public static void setSignUpProfilePicSkip(String signUpProfilePicSkip) {
        mSignUpProfilePicSkip = signUpProfilePicSkip;
    }

    public static String getSuggestion(int init) {
        return getBaseUrl() + mSuggestion + "?page=" + init;
    }

    public static void setSuggestion(String suggestion) {
        mSuggestion = suggestion;
    }

    public static String getProfilePic() {
        return getBaseUrl() + mProfilePic;
    }

    public static void setProfilePic(String profilePic) {
        mProfilePic = profilePic;
    }

    public static String getProfileWall() {
        return getBaseUrl() + mProfileWall;
    }

    public static void setProfileWall(String profileWall) {
        mProfileWall = profileWall;
    }

    public static String getBigImage() {
        return getBaseUrl() + mBigImage;
    }

    public static void setBigImage(String bigImage) {
        mBigImage = bigImage;
    }

    public static String getProfileCountLists(int init) {
        return getBaseUrl() + mProfileCountLists + "?page=" + init;
    }

    public static void setProfileCountLists(String profileCountLists) {
        mProfileCountLists = profileCountLists;
    }

    public static String getNewNotification() {
        return getBaseUrl() + mNewNotification;
    }

    public static void setNewNotification(String newNotification) {
        mNewNotification = newNotification;
    }

    public static String getChatBlock() {
        return getBaseUrl() + mChatBlock;
    }

    public static void setChatBlock(String chatBlock) {
        mChatBlock = chatBlock;
    }

    public static String getMy_blocks_list() {
        return getBaseUrl() + my_blocks_list;
    }

    public static void setMy_blocks_list(String my_blocks_list) {
        ApiUtil.my_blocks_list = my_blocks_list;
    }

    public static String getMy_blocks() {
        return getBaseUrl() + my_blocks;
    }

    public static void setMy_blocks(String my_blocks) {
        ApiUtil.my_blocks = my_blocks;
    }

    public static String getChangePassword() {
        return getBaseUrl() + mChangePassword;
    }

    public static void setChangePassword(String changePassword) {
        mChangePassword = changePassword;
    }

    public static String getContactUs() {
        return getBaseUrl() + mContactUs;
    }

    public static void setContactUs(String contactUs) {
        mContactUs = contactUs;
    }

    public static String getLogout() {
        return getBaseUrl() + mLogout;
    }

    public static void setLogout(String logout) {
        mLogout = logout;
    }

    public static String getChangePhoneNumber() {
        return getBaseUrl() + mChangePhoneNumber;
    }

    public static void setChangePhoneNumber(String changePhoneNumber) {
        mChangePhoneNumber = changePhoneNumber;
    }

    public static String getOtp() {
        return getBaseUrl() + mOtp;
    }

    public static void setOtp(String otp) {
        mOtp = otp;
    }

    public static String getChatAllow() {
        return getBaseUrl() + mChatAllow;
    }

    public static void setChatAllow(String chatAllow) {
        mChatAllow = chatAllow;
    }

    public static String getBlock() {
        return getBaseUrl() + mBlock;
    }

    public static void setBlock(String chatBlock) {
        mBlock = chatBlock;
    }

    public static String getCheckFaceMatch() {
        return getBaseUrl() + mCheckFaceMatch;
    }


    public static void setCheckFaceMatch(String checkFaceMatch) {
        mCheckFaceMatch = checkFaceMatch;
    }

    public static String getPostDelete() {
        return getBaseUrl() + mPostDelete;
    }

    public static void setPostDelete(String postDelete) {
        mPostDelete = postDelete;
    }

    public static String getUnFollowPost() {
        return getBaseUrl() + mUnFollowPost;
    }

    public static void setUnFollowPost(String unFollowPost) {
        mUnFollowPost = unFollowPost;
    }

    public static String getCommentDelete() {
        return getBaseUrl() + mCommentDelete;
    }

    public static void setCommentDelete(String commentDelete) {
        mCommentDelete = commentDelete;
    }

    public static String getFollow() {
        return getBaseUrl() + mFollow;
    }

    public static void setFollow(String follow) {
        mFollow = follow;
    }

    public static String getChatImageUpload() {
        return getBaseUrl() + mChatImageUpload;
    }

    public static void setChatImageUpload(String chatImageUpload) {
        mChatImageUpload = chatImageUpload;
    }

    public static String getConversationDelete() {
        return getBaseUrl() + mConversationDelete;
    }

    public static void setConversationDelete(String conversationDelete) {
        mConversationDelete = conversationDelete;
    }

    public static String getForceReadConversation() {
        return getBaseUrl() + mForceReadConversation;
    }

    public static void setForceReadConversation(String forceReadConversation) {
        mForceReadConversation = forceReadConversation;
    }

    public static String getTyping() {
        return getBaseUrl() + mTyping;
    }

    public static void setTyping(String typing) {
        mTyping = typing;
    }

    public static String getPostSingle(int postID) {
        return getBaseUrl() + mPostSingle + "/" + postID;
    }

    public static void setPostSingle(String postSingle) {
        mPostSingle = postSingle;
    }

    public static String getSearch(String search, String type, int init) {

        String query = null;
        try {
            query = URLEncoder.encode(search, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return getBaseUrl() + mSearch + "/" + query + "/" + type + "?page=" + init;
    }

    public static void setSearch(String search) {
        mSearch = search;
    }

    public static String getPostsPool() {
        return getBaseUrl() + mPostsPool;
    }

    public static void setPostsPool(String postsPool) {
        mPostsPool = postsPool;
    }

    public static String getOnline_conversation() {
        return getBaseUrl() + mOnline_conversation;
    }

    public static void setOnline_conversation(String online_conversation) {
        mOnline_conversation = online_conversation;
    }

    public static String getConversationInit() {
        return getBaseUrl() + mConversationInit;
    }

    public static void setConversationInit(String conversationInit) {
        mConversationInit = conversationInit;
    }

    public static String getSettings() {
        return getBaseUrl() + mSettings;
    }

    public static void setSettings(String settings) {
        mSettings = settings;
    }

    public static String getRandomProfiles(int init) {
        return getBaseUrl() + mRandomProfiles + "?page=" + init;
    }

    public static void setRandomProfiles(String randomProfiles) {
        mRandomProfiles = randomProfiles;
    }

    public static String getNotificationUrl() {
        return getBaseUrl() + mNotification;
    }

    public static void setNotificationUrl(String notification) {
        mNotification = notification;
    }

    public static String getReadConversationUrl() {
        return getBaseUrl() + mReadConversationUrl;
    }

    public static void setReadConversationUrl(String readConversationUrl) {
        mReadConversationUrl = readConversationUrl;
    }

    public static String getPostImageUrl() {
        return getBaseUrl() + mPostImageUrl;
    }

    public static void setPostImageUrl(String postImageUrl) {
        mPostImageUrl = postImageUrl;
    }

    public static String getPostTextUrl() {
        return getBaseUrl() + mPostTextUrl;
    }

    public static void setPostTextUrl(String mpostText) {
        ApiUtil.mPostTextUrl = mpostText;
    }

    public static String getfollowing() {
        return getBaseUrl() + mFollowing;
    }

    public static void setfollowing(String mfollowing) {
        mFollowing = mfollowing;
    }

    public static String getNewMessage() {
        return getBaseUrl() + mNewMessage;
    }

    public static void setNewMessage(String newMessage) {
        ApiUtil.mNewMessage = newMessage;
    }

    public static String getSimpleMessage() {
        return getBaseUrl() + mSimpleMessage;
    }

    public static void setSimpleMessage(String simpleMessage) {
        mSimpleMessage = simpleMessage;
    }

    public static String getComplexMessage() {
        return getBaseUrl() + mComplexMessage;
    }

    public static void setComplexMessage(String complexMessage) {
        mComplexMessage = complexMessage;
    }

    public static String getCommentUrl(int postID, int init) {
        return getBaseUrl() + mCommentUrl + "/" + postID + "?page=" + init;
    }

    public static String getCommentPostUrl() {
        return getBaseUrl() + mCommentUrl;
    }

    public static void setCommentUrl(String commentUrl) {
        mCommentUrl = commentUrl;
    }

    public static String getLikesUrl(int init) {
        return getBaseUrl() + mLikesUrl + "?page=" + init;
    }

    public static void setLikesUrl(String likesUrl) {
        mLikesUrl = likesUrl;
    }

    public static String getUserId() {
        return mUserId;
    }

    public static void setUserId(String userId) {
        mUserId = userId;
    }

    public static String getUserPostUrl(String username, int init) {
        return getBaseUrl() + mPostUrl + "/" + username + "?page=" + init;
    }

    public static String getName() {
        return mName;
    }

    public static void setName(String name) {
        mName = name;
    }

    public static String getUserName() {
        return mUserName;
    }

    public static void setUserName(String userName) {
        mUserName = userName;
    }

    public static String getProfileSmall() {
        return mProfileSmall;
    }

    public static void setProfileSmall(String profileSmall) {
        mProfileSmall = profileSmall;
    }

    private static String getToken() {
        return mToken;
    }

    public static void setToken(String token) {
        mToken = token;
    }

    private static String getBaseUrl() {
        return mBaseUrl + getToken() + "/";
    }

    public static void setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    public static String getPostUrl(int init) {
        return getBaseUrl() + mPostUrl + "?page=" + init;
    }

    public static void setPostUrl(String postUrl) {
        mPostUrl = postUrl;
    }

    public static String getChatListUrl() {
        return getBaseUrl() + mChatListUrl;
    }

    public static void setChatListUrl(String chatListUrl) {
        mChatListUrl = chatListUrl;
    }

    public static String getOnlineListUrl() {
        return getBaseUrl() + mOnlineListUrl;
    }

    public static void setOnlineListUrl(String onlineListUrl) {
        mOnlineListUrl = onlineListUrl;
    }

    public static String getProfileUrl(String profileUrl) {
        return getBaseUrl() + "@" + profileUrl;
    }

    public static String getMyProfileUrl() {
        return getBaseUrl() + getUserName();
    }

    public static String getLikeUrl() {
        return getBaseUrl() + mLikeUrl;
    }

    public static void setLikeUrl(String likeUrl) {
        mLikeUrl = likeUrl;
    }

    public static String getTrendUrl(int init) {
        return getBaseUrl() + mTrendUrl + "?page=" + init;
    }

    public static void setTrendUrl(String trendUrl) {
        mTrendUrl = trendUrl;
    }

}
