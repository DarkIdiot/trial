//package com.darkidiot.curator;
//
//import java.util.ArrayList;
//import java.util.EnumSet;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import org.apache.zookeeper.OpResult;
//
//public abstract class KeeperException extends Exception {
//    private List<OpResult> results;
//    private Code code;
//    private String path;
//
//    public static KeeperException create(Code code, String path) {
//        KeeperException r = create(code);
//        r.path = path;
//        return r;
//    }
//
//    /** @deprecated */
//    @Deprecated
//    public static KeeperException create(int code, String path) {
//        KeeperException r = create(Code.get(code));
//        r.path = path;
//        return r;
//    }
//
//    /** @deprecated */
//    @Deprecated
//    public static KeeperException create(int code) {
//        return create(Code.get(code));
//    }
//
//    public static KeeperException create(Code code) {
//        switch(null.$SwitchMap$org$apache$zookeeper$KeeperException$Code[code.ordinal()]) {
//        case 1:
//            return new SystemErrorException();
//        case 2:
//            return new RuntimeInconsistencyException();
//        case 3:
//            return new DataInconsistencyException();
//        case 4:
//            return new ConnectionLossException();
//        case 5:
//            return new MarshallingErrorException();
//        case 6:
//            return new UnimplementedException();
//        case 7:
//            return new OperationTimeoutException();
//        case 8:
//            return new NewConfigNoQuorum();
//        case 9:
//            return new ReconfigInProgress();
//        case 10:
//            return new BadArgumentsException();
//        case 11:
//            return new APIErrorException();
//        case 12:
//            return new NoNodeException();
//        case 13:
//            return new NoAuthException();
//        case 14:
//            return new BadVersionException();
//        case 15:
//            return new NoChildrenForEphemeralsException();
//        case 16:
//            return new NodeExistsException();
//        case 17:
//            return new InvalidACLException();
//        case 18:
//            return new AuthFailedException();
//        case 19:
//            return new NotEmptyException();
//        case 20:
//            return new SessionExpiredException();
//        case 21:
//            return new InvalidCallbackException();
//        case 22:
//            return new SessionMovedException();
//        case 23:
//            return new NotReadOnlyException();
//        case 24:
//            return new EphemeralOnLocalSessionException();
//        case 25:
//            return new NoWatcherException();
//        case 26:
//        default:
//            throw new IllegalArgumentException("Invalid exception code");
//        }
//    }
//
//    /** @deprecated */
//    @Deprecated
//    public void setCode(int code) {
//        this.code = Code.get(code);
//    }
//
//    static String getCodeMessage(Code code) {
//        switch(null.$SwitchMap$org$apache$zookeeper$KeeperException$Code[code.ordinal()]) {
//        case 1:
//            return "SystemError";
//        case 2:
//            return "RuntimeInconsistency";
//        case 3:
//            return "DataInconsistency";
//        case 4:
//            return "ConnectionLoss";
//        case 5:
//            return "MarshallingError";
//        case 6:
//            return "Unimplemented";
//        case 7:
//            return "OperationTimeout";
//        case 8:
//            return "NewConfigNoQuorum";
//        case 9:
//            return "ReconfigInProgress";
//        case 10:
//            return "BadArguments";
//        case 11:
//            return "APIError";
//        case 12:
//            return "NoNode";
//        case 13:
//            return "NoAuth";
//        case 14:
//            return "BadVersion";
//        case 15:
//            return "NoChildrenForEphemerals";
//        case 16:
//            return "NodeExists";
//        case 17:
//            return "InvalidACL";
//        case 18:
//            return "AuthFailed";
//        case 19:
//            return "Directory not empty";
//        case 20:
//            return "Session expired";
//        case 21:
//            return "Invalid callback";
//        case 22:
//            return "Session moved";
//        case 23:
//            return "Not a read-only call";
//        case 24:
//            return "Ephemeral node on local session";
//        case 25:
//            return "No such watcher";
//        case 26:
//            return "ok";
//        default:
//            return "Unknown error " + code;
//        }
//    }
//
//    public KeeperException(Code code) {
//        this.code = code;
//    }
//
//    KeeperException(Code code, String path) {
//        this.code = code;
//        this.path = path;
//    }
//
//    /** @deprecated */
//    @Deprecated
//    public int getCode() {
//        return this.code.code;
//    }
//
//    public Code code() {
//        return this.code;
//    }
//
//    public String getPath() {
//        return this.path;
//    }
//
//    public String getMessage() {
//        return this.path == null?"KeeperErrorCode = " + getCodeMessage(this.code):"KeeperErrorCode = " + getCodeMessage(this.code) + " for " + this.path;
//    }
//
//    void setMultiResults(List<OpResult> results) {
//        this.results = results;
//    }
//
//    public List<OpResult> getResults() {
//        return this.results != null?new ArrayList(this.results):null;
//    }
//
//    public static class NoWatcherException extends KeeperException {
//        public NoWatcherException() {
//            super(Code.NOWATCHER);
//        }
//
//        public NoWatcherException(String path) {
//            super(Code.NOWATCHER, path);
//        }
//    }
//
//    public static class UnimplementedException extends KeeperException {
//        public UnimplementedException() {
//            super(Code.UNIMPLEMENTED);
//        }
//    }
//
//    public static class SystemErrorException extends KeeperException {
//        public SystemErrorException() {
//            super(Code.SYSTEMERROR);
//        }
//    }
//
//    public static class EphemeralOnLocalSessionException extends KeeperException {
//        public EphemeralOnLocalSessionException() {
//            super(Code.EPHEMERALONLOCALSESSION);
//        }
//    }
//
//    public static class NotReadOnlyException extends KeeperException {
//        public NotReadOnlyException() {
//            super(Code.NOTREADONLY);
//        }
//    }
//
//    public static class SessionMovedException extends KeeperException {
//        public SessionMovedException() {
//            super(Code.SESSIONMOVED);
//        }
//    }
//
//    public static class UnknownSessionException extends KeeperException {
//        public UnknownSessionException() {
//            super(Code.UNKNOWNSESSION);
//        }
//    }
//
//    public static class SessionExpiredException extends KeeperException {
//        public SessionExpiredException() {
//            super(Code.SESSIONEXPIRED);
//        }
//    }
//
//    public static class RuntimeInconsistencyException extends KeeperException {
//        public RuntimeInconsistencyException() {
//            super(Code.RUNTIMEINCONSISTENCY);
//        }
//    }
//
//    public static class OperationTimeoutException extends KeeperException {
//        public OperationTimeoutException() {
//            super(Code.OPERATIONTIMEOUT);
//        }
//    }
//
//    public static class NotEmptyException extends KeeperException {
//        public NotEmptyException() {
//            super(Code.NOTEMPTY);
//        }
//
//        public NotEmptyException(String path) {
//            super(Code.NOTEMPTY, path);
//        }
//    }
//
//    public static class NoNodeException extends KeeperException {
//        public NoNodeException() {
//            super(Code.NONODE);
//        }
//
//        public NoNodeException(String path) {
//            super(Code.NONODE, path);
//        }
//    }
//
//    public static class NodeExistsException extends KeeperException {
//        public NodeExistsException() {
//            super(Code.NODEEXISTS);
//        }
//
//        public NodeExistsException(String path) {
//            super(Code.NODEEXISTS, path);
//        }
//    }
//
//    public static class NoChildrenForEphemeralsException extends KeeperException {
//        public NoChildrenForEphemeralsException() {
//            super(Code.NOCHILDRENFOREPHEMERALS);
//        }
//
//        public NoChildrenForEphemeralsException(String path) {
//            super(Code.NOCHILDRENFOREPHEMERALS, path);
//        }
//    }
//
//    public static class ReconfigInProgress extends KeeperException {
//        public ReconfigInProgress() {
//            super(Code.RECONFIGINPROGRESS);
//        }
//    }
//
//    public static class NewConfigNoQuorum extends KeeperException {
//        public NewConfigNoQuorum() {
//            super(Code.NEWCONFIGNOQUORUM);
//        }
//    }
//
//    public static class NoAuthException extends KeeperException {
//        public NoAuthException() {
//            super(Code.NOAUTH);
//        }
//    }
//
//    public static class MarshallingErrorException extends KeeperException {
//        public MarshallingErrorException() {
//            super(Code.MARSHALLINGERROR);
//        }
//    }
//
//    public static class InvalidCallbackException extends KeeperException {
//        public InvalidCallbackException() {
//            super(Code.INVALIDCALLBACK);
//        }
//    }
//
//    public static class InvalidACLException extends KeeperException {
//        public InvalidACLException() {
//            super(Code.INVALIDACL);
//        }
//
//        public InvalidACLException(String path) {
//            super(Code.INVALIDACL, path);
//        }
//    }
//
//    public static class DataInconsistencyException extends KeeperException {
//        public DataInconsistencyException() {
//            super(Code.DATAINCONSISTENCY);
//        }
//    }
//
//    public static class ConnectionLossException extends KeeperException {
//        public ConnectionLossException() {
//            super(Code.CONNECTIONLOSS);
//        }
//    }
//
//    public static class BadVersionException extends KeeperException {
//        public BadVersionException() {
//            super(Code.BADVERSION);
//        }
//
//        public BadVersionException(String path) {
//            super(Code.BADVERSION, path);
//        }
//    }
//
//    public static class BadArgumentsException extends KeeperException {
//        public BadArgumentsException() {
//            super(Code.BADARGUMENTS);
//        }
//
//        public BadArgumentsException(String path) {
//            super(Code.BADARGUMENTS, path);
//        }
//    }
//
//    public static class AuthFailedException extends KeeperException {
//        public AuthFailedException() {
//            super(Code.AUTHFAILED);
//        }
//    }
//
//    public static class APIErrorException extends KeeperException {
//        public APIErrorException() {
//            super(Code.APIERROR);
//        }
//    }
//
//    public static enum Code implements CodeDeprecated {
//        OK(0),
//        SYSTEMERROR(-1),
//        RUNTIMEINCONSISTENCY(-2),
//        DATAINCONSISTENCY(-3),
//        CONNECTIONLOSS(-4),
//        MARSHALLINGERROR(-5),
//        UNIMPLEMENTED(-6),
//        OPERATIONTIMEOUT(-7),
//        BADARGUMENTS(-8),
//        NEWCONFIGNOQUORUM(-13),
//        RECONFIGINPROGRESS(-14),
//        UNKNOWNSESSION(-12),
//        APIERROR(-100),
//        NONODE(-101),
//        NOAUTH(-102),
//        BADVERSION(-103),
//        NOCHILDRENFOREPHEMERALS(-108),
//        NODEEXISTS(-110),
//        NOTEMPTY(-111),
//        SESSIONEXPIRED(-112),
//        INVALIDCALLBACK(-113),
//        INVALIDACL(-114),
//        AUTHFAILED(-115),
//        SESSIONMOVED(-118),
//        NOTREADONLY(-119),
//        EPHEMERALONLOCALSESSION(-120),
//        NOWATCHER(-121);
//
//        private static final Map<Integer, Code> lookup = new HashMap();
//        private final int code;
//
//        private Code(int code) {
//            this.code = code;
//        }
//
//        public int intValue() {
//            return this.code;
//        }
//
//        public static Code get(int code) {
//            return (Code)lookup.get(Integer.valueOf(code));
//        }
//
//        static {
//            Iterator var0 = EnumSet.allOf(Code.class).iterator();
//
//            while(var0.hasNext()) {
//                Code c = (Code)var0.next();
//                lookup.put(Integer.valueOf(c.code), c);
//            }
//
//        }
//    }
//
//    /** @deprecated */
//    @Deprecated
//    public interface CodeDeprecated {
//
//
//        int Ok = 0;
//
//
//        int SystemError = -1;
//
//
//        int RuntimeInconsistency = -2;
//
//
//        int DataInconsistency = -3;
//
//
//        int ConnectionLoss = -4;
//
//
//        int MarshallingError = -5;
//
//
//        int Unimplemented = -6;
//
//
//        int OperationTimeout = -7;
//
//
//        int BadArguments = -8;
//
//
//        int UnknownSession = -12;
//
//
//        int NewConfigNoQuorum = -13;
//
//
//        int ReconfigInProgress = -14;
//
//
//        int APIError = -100;
//
//
//        int NoNode = -101;
//
//
//        int NoAuth = -102;
//
//
//        int BadVersion = -103;
//
//
//        int NoChildrenForEphemerals = -108;
//
//
//        int NodeExists = -110;
//
//
//        int NotEmpty = -111;
//
//
//        int SessionExpired = -112;
//
//
//        int InvalidCallback = -113;
//
//
//        int InvalidACL = -114;
//
//
//        int AuthFailed = -115;
//
//
//        int EphemeralOnLocalSession = -120;
//    }
//}
