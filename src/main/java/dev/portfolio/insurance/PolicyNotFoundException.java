package dev.portfolio.insurance;
public class PolicyNotFoundException extends RuntimeException {
    public PolicyNotFoundException(String number) { super("Policy not found: " + number); }
}
