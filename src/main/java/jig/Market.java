package jig;

/**
 * Created by Tyler on 12/23/15.
 */
public enum Market {

  EN_US("en-us");

  private final String market;

  Market(final String adultOption) {
    this.market = adultOption;
  }

  @Override
  public String toString() {
    return this.market;
  }

}
