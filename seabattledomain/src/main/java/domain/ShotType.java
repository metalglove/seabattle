/*
 * Sea Battle Start project.
 */
package domain;

/**
 * Indicate the result of a shot.
 *
 * @author Nico Kuijpers
 */
public enum ShotType {
  MISSED,   // Shot missed
  HIT,      // Shot hit
  SUNK,     // Ship sunk
  ALLSUNK;  // All ships sunk
}
