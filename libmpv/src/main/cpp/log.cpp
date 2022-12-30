#include "log.h"

#include <cstdlib>

void die(const char *msg)
{
    ALOGE("%s", msg);
    exit(1);
}
